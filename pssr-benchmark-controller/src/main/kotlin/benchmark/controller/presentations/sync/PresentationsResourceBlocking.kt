package benchmark.controller.presentations.sync

import benchmark.repository.PresentationRepository
import benchmark.view.presentations.JStachioView
import benchmark.view.presentations.PresentationsHtmlFlow
import benchmark.view.presentations.PresentationsKotlinX.kotlinXIter
import com.fizzed.rocker.runtime.OutputStreamOutput
import freemarker.template.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import io.smallrye.common.annotation.RunOnVirtualThread
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.StreamingOutput
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.trimou.engine.MustacheEngine

@RunOnVirtualThread
@Path("/presentations")
class PresentationsResourceBlocking
    @Inject
    constructor(
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

        @GET
        @Path("/rocker")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateRockerSync(): Response {
            val output =
                StreamingOutput { out ->
                    rocker
                        .presentations
                        .template(presentationsIter)
                        .render { contentType, charset -> OutputStreamOutput(contentType, out, charset) }
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/jstachio")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateJStachioSync(): Response {
            val output =
                StreamingOutput { out ->
                    JStachioView.presentationsWrite(presentationsModelJStachio, out)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/pebble")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplatePebbleSync(): Response {
            val output =
                StreamingOutput { out ->
                    pebbleView.evaluate(out.writer(), presentationsModelMap)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/freemarker")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateFreemarkerSync(): Response {
            val output =
                StreamingOutput { out ->
                    freemarkerView.process(presentationsModelMap, out.writer())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/trimou")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateTrimouSync(): Response {
            val output =
                StreamingOutput { out ->
                    trimouView.render(out.outputStreamWriter(), presentationsModelMap)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/velocity")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateVelocitySync(): Response {
            val output =
                StreamingOutput { out ->
                    viewVelocity.merge(presentationsModelVelocity, out.outputStreamWriter())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/thymeleaf")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateThymeleafSync(): Response {
            val output =
                StreamingOutput { out ->
                    thymeleafEngine.process("index-thymeleaf", presentationsModelThymeleaf, out.writer())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/htmlFlow")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateHtmlFlowSync(): Response {
            val output =
                StreamingOutput { out ->
                    PresentationsHtmlFlow
                        .htmlFlowTemplateIter
                        .setOut(out.outputStreamWriter())
                        .write(presentationsIter)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/kotlinx")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateKotlinXSync(): Response {
            val output =
                StreamingOutput { out ->
                    kotlinXIter(out.outputStreamWriter(), presentationsIter)
                }
            return Response.ok(output).build()
        }
    }
