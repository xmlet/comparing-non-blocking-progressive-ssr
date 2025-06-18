package benchmark

import benchmark.model.Presentation
import benchmark.model.Stock
import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepositoryMem
import benchmark.view.presentations.PresentationsHtmlFlow
import benchmark.view.stocks.StocksHtmlFlow
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup

open class HtmlFlow : BaseBenchmark() {
    private lateinit var stocks: Iterable<Stock>
    private lateinit var presentations: Iterable<Presentation>

    @Setup
    fun setup() {
        val presentationsRepository = PresentationRepositoryMem(0)
        val stockRepository = StockRepositoryMem(0)
        stocks = stockRepository.findAllIterable()
        presentations = presentationsRepository.findAllIterable()
    }

    @Benchmark
    fun stocks(): String {
        return StocksHtmlFlow.htmlFlowTemplateIter.render(stocks)
    }

    @Benchmark
    fun presentations(): String {
        return PresentationsHtmlFlow.htmlFlowTemplateIter.render(presentations)
    }
}