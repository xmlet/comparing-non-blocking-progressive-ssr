package benchmark.controller.presentations.reactive

import benchmark.repository.StockRepository
import benchmark.view.stocks.StocksHtmlFlow
import io.quarkus.qute.Location
import io.quarkus.qute.Template
import io.smallrye.mutiny.Multi
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/stocks/reactive")
class StocksResourceReactive
    @Inject
    constructor(
        stocks: StockRepository,
    ) {
        private val stocksIter = stocks.findAllIterable()
        private val stocksFlux = stocks.findAllReactive()

        @Location("qute/stocks")
        lateinit var template: Template

        @GET
        @Path("/qute")
        @Produces(MediaType.TEXT_HTML)
        fun getQute(): Multi<String> {
            return template.data("stocks", stocksIter).createMulti()
        }

        @GET
        @Path("/htmlFlow")
        @Produces(MediaType.TEXT_HTML)
        fun getHtmlFlow(): Multi<String> {
            val view = AppendableMulti().also { sink ->
                    StocksHtmlFlow.htmlFlowTemplate
                        .writeAsync(sink, stocksFlux)
                        .thenAccept { sink.close() }
                }
            return view.toMulti()
        }
    }
