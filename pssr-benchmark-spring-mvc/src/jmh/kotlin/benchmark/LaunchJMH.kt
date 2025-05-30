package benchmark

import benchmark.controller.presentations.DEFAULT_FREEMARKER_CONFIG
import benchmark.controller.presentations.DEFAULT_MUSTACHE_ENGINE
import benchmark.controller.presentations.DEFAULT_PEBBLE_ENGINE
import benchmark.controller.presentations.DEFAULT_THYMELEAF_ENGINE
import benchmark.controller.presentations.DEFAULT_VELOCITY_ENGINE
import benchmark.controller.presentations.sync.PresentationsResourceBlocking
import benchmark.controller.presentations.sync.StocksResourceBlocking
import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepositoryMem
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.StreamingOutput
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.reactive.function.server.ServerRequest
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

// java -jar build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT-jmh.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t 8
// -i 4 iterations
// -wi 4 warmup iterations
// -f 1 fork
// -r 2 run each iteration for 2 seconds
// -w 2 run each warmup iteration for 2 seconds.
// -t 8 worker threads
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Benchmark)
@SpringBootTest
open class LaunchJMH {
    @Param(
        "/presentations/rocker",
        "/presentations/jstachio",
        "/presentations/pebble",
        "/presentations/freemarker",
        "/presentations/trimou",
        "/presentations/velocity",
        "/presentations/thymeleaf",
        "/presentations/htmlFlow",
        "/presentations/kotlinx",
        "/stocks/rocker",
        "/stocks/jstachio",
        "/stocks/pebble",
        "/stocks/freemarker",
        "/stocks/trimou",
        "/stocks/velocity",
        "/stocks/thymeleaf",
        "/stocks/htmlFlow",
        "/stocks/kotlinx",
    )
    lateinit var route: String
    private val timeout = 0L
    private val presentationsRepositoryMem = PresentationRepositoryMem(timeout)
    private val stocksRepositoryMem = StockRepositoryMem(timeout)

    private val presentationsRouter =
        PresentationsResourceBlocking(
            freemarkerConfig = DEFAULT_FREEMARKER_CONFIG,
            pebbleEngine = DEFAULT_PEBBLE_ENGINE,
            thymeleafEngine = DEFAULT_THYMELEAF_ENGINE,
            mustacheEngine = DEFAULT_MUSTACHE_ENGINE,
            velocityEngine = DEFAULT_VELOCITY_ENGINE,
            presentations = presentationsRepositoryMem,
        )

    private val stocksRouter =
        StocksResourceBlocking(
            freemarkerConfig = DEFAULT_FREEMARKER_CONFIG,
            pebbleEngine = DEFAULT_PEBBLE_ENGINE,
            thymeleafEngine = DEFAULT_THYMELEAF_ENGINE,
            mustacheEngine = DEFAULT_MUSTACHE_ENGINE,
            velocityEngine = DEFAULT_VELOCITY_ENGINE,
            stocks = stocksRepositoryMem,
        )

    @Benchmark
    fun benchRoute(): String {
        val request = MockServerHttpRequest.get(route).build()
        val exchange = MockServerWebExchange.from(request)
        val serverRequest = ServerRequest.create(exchange, DefaultServerCodecConfigurer().readers)

        val response = routeRequest(serverRequest)
        val entity = response.entity

        if (entity is StreamingOutput) {
            val outputStream = ByteArrayOutputStream()
            entity.write(outputStream)
            return outputStream.toString(Charsets.UTF_8.name())
        } else {
            throw IllegalStateException("Response entity is not a StreamingOutput")
        }
    }

    private fun routeRequest(serverRequest: ServerRequest): Response {
        val res =
            when (serverRequest.requestPath().value()) {
                "/presentations/rocker" -> presentationsRouter.handleTemplateRockerSync()
                "/presentations/jstachio" -> presentationsRouter.handleTemplateJStachioSync()
                "/presentations/pebble" -> presentationsRouter.handleTemplatePebbleSync()
                "/presentations/freemarker" -> presentationsRouter.handleTemplateFreemarkerSync()
                "/presentations/trimou" -> presentationsRouter.handleTemplateTrimouSync()
                "/presentations/velocity" -> presentationsRouter.handleTemplateVelocitySync()
                "/presentations/thymeleaf" -> presentationsRouter.handleTemplateThymeleafSync()
                "/presentations/htmlFlow" -> presentationsRouter.handleTemplateHtmlFlowSync()
                "/presentations/kotlinx" -> presentationsRouter.handleTemplateKotlinXSync()
                "/stocks/rocker" -> stocksRouter.handleTemplateRockerSync()
                "/stocks/jstachio" -> stocksRouter.handleTemplateJStachioSync()
                "/stocks/pebble" -> stocksRouter.handleTemplatePebbleSync()
                "/stocks/freemarker" -> stocksRouter.handleTemplateFreemarkerSync()
                "/stocks/trimou" -> stocksRouter.handleTemplateTrimouSync()
                "/stocks/velocity" -> stocksRouter.handleTemplateVelocitySync()
                "/stocks/thymeleaf" -> stocksRouter.handleTemplateThymeleafSync()
                "/stocks/htmlFlow" -> stocksRouter.handleTemplateHtmlFlowSync()
                "/stocks/kotlinx" -> stocksRouter.handleTemplateKotlinXSync()
                else -> throw IllegalArgumentException("Unknown route: $route")
            }
        return res
    }
}
