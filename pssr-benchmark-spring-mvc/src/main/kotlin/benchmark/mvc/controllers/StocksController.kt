package benchmark.mvc.controllers

import benchmark.controller.presentations.sync.outputStreamWriter
import benchmark.repository.StockRepository
import benchmark.view.stocks.JStachioView
import benchmark.view.stocks.StocksHtmlFlow
import benchmark.view.stocks.StocksKotlinX
import com.fizzed.rocker.runtime.OutputStreamOutput
import freemarker.template.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.trimou.engine.MustacheEngine

@RestController
@RequestMapping("/stocks")
class StocksController(
    private val freemarkerConfig: Configuration,
    private val pebbleEngine: PebbleEngine,
    private val thymeleafEngine: TemplateEngine,
    private val mustacheEngine: MustacheEngine,
    private val velocityEngine: VelocityEngine,
    private val stocks: StockRepository,
) {
    private val pebbleView = pebbleEngine.getTemplate("stocks")
    private val freemarkerView = freemarkerConfig.getTemplate("templates/freemarker/stocks-freemarker.ftl")
    private val trimouView = mustacheEngine.getMustache("stocks")
    private val viewVelocity = velocityEngine.getTemplate("templates/velocity/stocks-velocity.vm", "UTF-8")

    /**
     * Data models
     */
    private val stocksIter = stocks.findAllIterable()
    private val stocksModelJStachio = JStachioView.StocksModel(stocksIter)
    private val stocksModelMap: Map<String, Any> = mutableMapOf("stocks" to stocksIter)
    private val stocksModelVelocity = VelocityContext(stocksModelMap)
    private val stocksModelThymeleaf = Context().apply { setVariable("stocks", stocksIter) }

    @GetMapping("/rocker", produces = ["text/html"])
    fun handleTemplateRockerSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                rocker
                    .stocks
                    .template(stocksIter)
                    .render { contentType, charset -> OutputStreamOutput(contentType, out, charset) }
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/jstachio", produces = ["text/html"])
    fun handleTemplateJStachioSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                JStachioView.stocksWrite(stocksModelJStachio, out)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/pebble", produces = ["text/html"])
    fun handleTemplatePebbleSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                pebbleView.evaluate(out.writer(), stocksModelMap)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/freemarker", produces = ["text/html"])
    fun handleTemplateFreemarkerSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                freemarkerView.process(stocksModelMap, out.writer())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/trimou", produces = ["text/html"])
    fun handleTemplateTrimouSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                trimouView.render(out.outputStreamWriter(), stocksModelMap)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/velocity", produces = ["text/html"])
    fun handleTemplateVelocitySync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                viewVelocity.merge(stocksModelVelocity, out.outputStreamWriter())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/thymeleaf", produces = ["text/html"])
    fun handleTemplateThymeleafSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                thymeleafEngine.process("stocks-thymeleaf", stocksModelThymeleaf, out.writer())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/htmlFlow", produces = ["text/html"])
    fun handleTemplateHtmlFlowSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                StocksHtmlFlow
                    .htmlFlowTemplateIter
                    .setOut(out.outputStreamWriter())
                    .write(stocksIter)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/kotlinx", produces = ["text/html"])
    fun handleTemplateKotlinXSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                StocksKotlinX.kotlinXIterable(out.outputStreamWriter(), stocksIter)
            }
        return ResponseEntity.ok(stream)
    }
}
