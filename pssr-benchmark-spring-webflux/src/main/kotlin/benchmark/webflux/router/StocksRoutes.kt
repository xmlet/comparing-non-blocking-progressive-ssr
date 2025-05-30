package benchmark.webflux.router

import benchmark.repository.StockRepository
import benchmark.view.appendable.AppendableSink
import benchmark.view.appendable.OutputStreamSink
import benchmark.view.appendable.WriterSink
import benchmark.view.stocks.JStachioView
import benchmark.view.stocks.JStachioView.StocksModel
import benchmark.view.stocks.StocksHtmlFlow
import benchmark.view.stocks.StocksKotlinX
import com.fizzed.rocker.runtime.OutputStreamOutput
import freemarker.template.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import io.reactivex.rxjava3.core.BackpressureStrategy.DROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable
import org.trimou.engine.MustacheEngine
import reactor.core.publisher.Mono

/**
 * Since JAX-RS does not support Project Reactor, we use this class to implement NIO routes using Spring WebFlux.
 *
 * Taken from: https://github.com/xmlet/spring-webflux-comparing-template-engines
 */
@Component
class StocksRoutes(
    freemarkerConfig: Configuration,
    pebbleEngine: PebbleEngine,
    private val thymeleafEngine: TemplateEngine,
    mustacheEngine: MustacheEngine,
    velocityEngine: VelocityEngine,
    stocks: StockRepository,
) {
    /**
     * We are using next one for synchronous blocking render.
     * We need to release calling thread to proceed request handling and return Publisher<String> with HTML.
     * Using Dispatchers.Unconfined on Blocking IO will prevent Progressive Rendering.
     */
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * It executes the initial continuation of a coroutine in the current
     * call-frame and lets the coroutine resume in whatever thread.
     * Better performance but not suitable for blocking IO, only for NIO.
     * NIO will release threads to perform other task.
     */
    private val unconf = CoroutineScope(Dispatchers.Unconfined)

    /**
     * Views
     */
    private val pebbleView = pebbleEngine.getTemplate("stocks")
    private val freemarkerView = freemarkerConfig.getTemplate("templates/freemarker/stocks-freemarker.ftl")
    private val trimouView = mustacheEngine.getMustache("stocks")
    private val viewVelocity = velocityEngine.getTemplate("templates/velocity/stocks-velocity.vm", "UTF-8")

    /**
     * Data models
     */
    private val stocksFlux = stocks.findAllReactive()
    private val stocksIter = stocks.findAllIterable()
    private val stocksModelJStachio = StocksModel(stocksIter)
    private val stocksModelMap: Map<String, Any> = mutableMapOf("stocks" to stocksIter)
    private val stocksModelVelocity = VelocityContext(stocksModelMap)
    private val stocksModelThymeleaf = Context().apply { setVariable("stocks", stocksIter) }
    private val presentationsFlow = stocksFlux.toFlowable(DROP).asFlow()
    private val presentationModelThymeleafRx = mapOf<String, Any>("stocks" to ReactiveDataDriverContextVariable(stocksFlux, 1))

    @Bean
    fun stocksRouter() =
        router {
            /*
             * Thymeleaf
             */
            GET("/stocks/thymeleaf") { handleTemplateThymeleaf() }
            GET("/stocks/thymeleaf/sync") { handleTemplateThymeleafSync() }
            GET("/stocks/thymeleaf/virtualSync") { handleTemplateThymeleafVirtualSync() }
            /*
             * HtmlFlow
             */
            GET("/stocks/htmlFlow") { handleTemplateHtmlFlowFromFlux() }
            GET("/stocks/htmlFlow/suspending") { handleTemplateHtmlFlowSuspending() }
            GET("/stocks/htmlFlow/sync") { handleTemplateHtmlFlowSync() }
            GET("/stocks/htmlFlow/virtualSync") { handleTemplateHtmlFlowVirtual() }
            /*
             * KotlinX
             */
            GET("/stocks/kotlinx") { handleTemplateKotlinX() } // Async non-blocking BUT returns MALL FORMED HTML
            GET("/stocks/kotlinx/sync") { handleTemplateKotlinXSync() }
            GET("/stocks/kotlinx/virtualSync") { handleTemplateKotlinXVirtualSync() }
            /*
             * Others that do NOT support data models with Asynchronous APIs.
             * Those use sync blocking approaches running on different Dispatcher and other thread pool,
             */
            GET("/stocks/rocker/sync") { handleTemplateRockerSync() }
            GET("/stocks/rocker/virtualSync") { handleTemplateRockerVirtualSync() }

            GET("/stocks/jstachio/sync") { handleTemplateJStachioSync() }
            GET("/stocks/jstachio/virtualSync") { handleTemplateJStachioVirtualSync() }

            GET("/stocks/pebble/sync") { handleTemplatePebbleSync() }
            GET("/stocks/pebble/virtualSync") { handleTemplatePebbleVirtualSync() }

            GET("/stocks/freemarker/sync") { handleTemplateFreemarkerSync() }
            GET("/stocks/freemarker/virtualSync") { handleTemplateFreemarkerVirtualSync() }

            GET("/stocks/trimou/sync") { handleTemplateTrimouSync() }
            GET("/stocks/trimou/virtualSync") { handleTemplateTrimouVirtualSync() }

            GET("/stocks/velocity/sync") { handleTemplateVelocitySync() }
            GET("/stocks/velocity/virtualSync") { handleTemplateVelocityVirtualSync() }
        }

    private fun handleTemplateRockerSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                scope.launch {
                    rocker
                        .stocks
                        .template(stocksIter)
                        .render { contentType, charset -> OutputStreamOutput(contentType, it, charset) }
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateRockerVirtualSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                Thread.startVirtualThread {
                    rocker
                        .stocks
                        .template(stocksIter)
                        .render { contentType, charset -> OutputStreamOutput(contentType, it, charset) }
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateJStachioSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                scope.launch {
                    JStachioView.stocksWrite(stocksModelJStachio, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateJStachioVirtualSync(): Mono<ServerResponse> {
        val out =
            OutputStreamSink().also {
                Thread.startVirtualThread {
                    JStachioView.stocksWrite(stocksModelJStachio, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplatePebbleSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    pebbleView.evaluate(it, stocksModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplatePebbleVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    pebbleView.evaluate(it, stocksModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateFreemarkerSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    freemarkerView.process(stocksModelMap, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateFreemarkerVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    freemarkerView.process(stocksModelMap, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateTrimouSync(): Mono<ServerResponse> {
        val out =
            AppendableSink().also {
                scope.launch {
                    trimouView.render(it, stocksModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateTrimouVirtualSync(): Mono<ServerResponse> {
        val out =
            AppendableSink().also {
                Thread.startVirtualThread {
                    trimouView.render(it, stocksModelMap)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateVelocitySync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    viewVelocity.merge(stocksModelVelocity, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateVelocityVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    viewVelocity.merge(stocksModelVelocity, it)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleafSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                scope.launch {
                    thymeleafEngine.process("stocks-thymeleaf", stocksModelThymeleaf, it)
                    it.close()
                }
            }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleafVirtualSync(): Mono<ServerResponse> {
        val out =
            WriterSink().also {
                Thread.startVirtualThread {
                    thymeleafEngine.process("stocks-thymeleaf", stocksModelThymeleaf, it)
                    it.close()
                }
            }

        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(out.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateThymeleaf(): Mono<ServerResponse> {
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .render("stocks-thymeleaf", presentationModelThymeleafRx)
    }

    private fun handleTemplateHtmlFlowSync(): Mono<ServerResponse> {
        /*
         * We need another co-routine in another thread (this one is blocking IO) to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view =
            AppendableSink().also {
                scope.launch {
                    StocksHtmlFlow
                        .htmlFlowTemplateSync
                        .setOut(it)
                        .write(stocksFlux)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowVirtual(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                Thread.startVirtualThread {
                    StocksHtmlFlow
                        .htmlFlowTemplateIter
                        .setOut(it)
                        .write(stocksIter)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowFromFlux(): Mono<ServerResponse> {
        val view =
            AppendableSink().also { sink ->
                StocksHtmlFlow
                    .htmlFlowTemplate
                    .writeAsync(sink, stocksFlux)
                    .thenAccept { sink.close() }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateHtmlFlowSuspending(): Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         * Here we are using Unconfined running in same therad and avoiding context switching.
         * That's ok since we are NOT blocking on htmlFlowTemplateSuspending.
         */
        val view =
            AppendableSink().also {
                unconf.launch {
                    StocksHtmlFlow
                        .htmlFlowTemplateSuspending
                        .write(it, presentationsFlow)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinXSync(): Mono<ServerResponse> {
        /*
         * We need another co-routine to render concurrently and ensure
         * progressive server-side rendering (PSSR)
         */
        val view =
            AppendableSink().also {
                scope.launch {
                    StocksKotlinX.kotlinXSync(it, stocksFlux)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinX(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                StocksKotlinX.kotlinXReactive(it, stocksFlux)
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }

    private fun handleTemplateKotlinXVirtualSync(): Mono<ServerResponse> {
        val view =
            AppendableSink().also {
                Thread.startVirtualThread {
                    StocksKotlinX.kotlinXIterable(it, stocksIter)
                    it.close()
                }
            }
        return ServerResponse
            .ok()
            .contentType(MediaType.TEXT_HTML)
            .body(view.asFlux(), object : ParameterizedTypeReference<String>() {})
    }
}
