package benchmark.controller.presentations.sync

import benchmark.repository.StockRepository
import benchmark.view.stocks.JStachioView
import benchmark.view.stocks.StocksHtmlFlow
import benchmark.view.stocks.StocksKotlinX
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

@Path("/stocks")
@RunOnVirtualThread
class StocksResourceBlocking
    @Inject
    constructor(
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

        @GET
        @Path("/rocker")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateRockerSync(): Response {
            val output =
                StreamingOutput { out ->
                    rocker
                        .stocks
                        .template(stocksIter)
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
                    JStachioView.stocksWrite(stocksModelJStachio, out)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/pebble")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplatePebbleSync(): Response {
            val output =
                StreamingOutput { out ->
                    pebbleView.evaluate(out.writer(), stocksModelMap)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/freemarker")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateFreemarkerSync(): Response {
            val output =
                StreamingOutput { out ->
                    freemarkerView.process(stocksModelMap, out.writer())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/trimou")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateTrimouSync(): Response {
            val output =
                StreamingOutput { out ->
                    trimouView.render(out.outputStreamWriter(), stocksModelMap)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/velocity")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateVelocitySync(): Response {
            val output =
                StreamingOutput { out ->
                    viewVelocity.merge(stocksModelVelocity, out.outputStreamWriter())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/thymeleaf")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateThymeleafSync(): Response {
            val output =
                StreamingOutput { out ->
                    thymeleafEngine.process("stocks-thymeleaf", stocksModelThymeleaf, out.writer())
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/htmlFlow")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateHtmlFlowSync(): Response {
            val output =
                StreamingOutput { out ->
                    StocksHtmlFlow
                        .htmlFlowTemplateIter
                        .setOut(out.outputStreamWriter())
                        .write(stocksIter)
                }
            return Response.ok(output).build()
        }

        @GET
        @Path("/kotlinx")
        @Produces(MediaType.TEXT_HTML)
        fun handleTemplateKotlinXSync(): Response {
            val output =
                StreamingOutput { out ->
                    StocksKotlinX.kotlinXIterable(out.outputStreamWriter(), stocksIter)
                }
            return Response.ok(output).build()
        }
    }
