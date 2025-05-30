package benchmark.mvc.controllers

import benchmark.controller.presentations.sync.outputStreamWriter
import benchmark.repository.PresentationRepository
import benchmark.view.presentations.JStachioView
import benchmark.view.presentations.PresentationsHtmlFlow.htmlFlowTemplateIter
import benchmark.view.presentations.PresentationsKotlinX.kotlinXIter
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
@RequestMapping("/presentations")
class PresentationsController(
    private val freemarkerConfig: Configuration,
    private val pebbleEngine: PebbleEngine,
    private val thymeleafEngine: TemplateEngine,
    private val mustacheEngine: MustacheEngine,
    private val velocityEngine: VelocityEngine,
    private val presentations: PresentationRepository,
) {
    private val pebbleView = pebbleEngine.getTemplate("presentations")
    private val freemarkerView = freemarkerConfig.getTemplate("templates/freemarker/index-freemarker.ftl")
    private val trimouView = mustacheEngine.getMustache("presentations")
    private val viewVelocity = velocityEngine.getTemplate("templates/velocity/presentations-velocity.vm", "UTF-8")

    /**
     * Data models
     */
    private val presentationsIter = presentations.findAllIterable()
    private val presentationsModelJStachio = JStachioView.PresentationsModel(presentationsIter)
    private val presentationsModelMap: Map<String, Any> = mutableMapOf("presentations" to presentationsIter)
    private val presentationsModelVelocity = VelocityContext(presentationsModelMap)
    private val presentationsModelThymeleaf = Context().apply { setVariable("presentations", presentationsIter) }

    @GetMapping("/rocker", produces = ["text/html"])
    fun handleTemplateRockerSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                rocker
                    .presentations
                    .template(presentationsIter)
                    .render { contentType, charset -> OutputStreamOutput(contentType, out, charset) }
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/jstachio", produces = ["text/html"])
    fun handleTemplateJStachioSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                JStachioView.presentationsWrite(presentationsModelJStachio, out)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/pebble", produces = ["text/html"])
    fun handleTemplatePebbleSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                pebbleView.evaluate(out.writer(), presentationsModelMap)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/freemarker", produces = ["text/html"])
    fun handleTemplateFreemarkerSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                freemarkerView.process(presentationsModelMap, out.writer())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/trimou", produces = ["text/html"])
    fun handleTemplateTrimouSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                trimouView.render(out.outputStreamWriter(), presentationsModelMap)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/velocity", produces = ["text/html"])
    fun handleTemplateVelocitySync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                viewVelocity.merge(presentationsModelVelocity, out.outputStreamWriter())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/thymeleaf", produces = ["text/html"])
    fun handleTemplateThymeleafSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                thymeleafEngine.process("index-thymeleaf", presentationsModelThymeleaf, out.writer())
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/htmlFlow", produces = ["text/html"])
    fun handleTemplateHtmlFlowSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                htmlFlowTemplateIter
                    .setOut(out.outputStreamWriter())
                    .write(presentationsIter)
            }
        return ResponseEntity.ok(stream)
    }

    @GetMapping("/kotlinx", produces = ["text/html"])
    fun handleTemplateKotlinXSync(): ResponseEntity<StreamingResponseBody> {
        val stream =
            StreamingResponseBody { out ->
                kotlinXIter(out.outputStreamWriter(), presentationsIter)
            }
        return ResponseEntity.ok(stream)
    }
}
