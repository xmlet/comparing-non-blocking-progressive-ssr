package benchmark

import benchmark.model.Presentation
import benchmark.model.Stock
import benchmark.view.presentations.JStachioPresentationsTemplate
import benchmark.view.presentations.PresentationsJstachio
import benchmark.view.stocks.JStachioStocksTemplate
import benchmark.view.stocks.StocksJstachio
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup

open class Jstachio : BaseBenchmark() {
    private lateinit var stocksModel: StocksJstachio.StocksModel
    private lateinit var presentationsModel: PresentationsJstachio.PresentationsModel

    private lateinit var stocksTemplate: JStachioStocksTemplate
    private lateinit var presentationsTemplate: JStachioPresentationsTemplate

    private lateinit var stocks: Iterable<Stock>
    private lateinit var presentations: Iterable<Presentation>

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)

        stocks = stockRepository.findAllIterable()
        presentations = presentationsRepository.findAllIterable()

        stocksModel = StocksJstachio.StocksModel(stocks)
        presentationsModel = PresentationsJstachio.PresentationsModel(presentations)

        stocksTemplate = JStachioStocksTemplate.of()
        presentationsTemplate = JStachioPresentationsTemplate.of()
    }

    @Benchmark
    fun stocks(): String {
        return stocksTemplate.execute(stocksModel)
    }

    @Benchmark
    fun presentations(): String {
        return presentationsTemplate.execute(presentationsModel)
    }
}