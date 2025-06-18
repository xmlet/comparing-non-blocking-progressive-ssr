package benchmark

import benchmark.model.Presentation
import benchmark.model.Stock
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup

open class Rocker : BaseBenchmark() {
    private lateinit var stocks : Iterable<Stock>
    private lateinit var presentations : Iterable<Presentation>

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)
        stocks = stockRepository.findAllIterable()
        presentations = presentationsRepository.findAllIterable()
    }

    @Benchmark
    fun stocks(): String {
        return rocker
            .stocks
            .template(stocks)
            .render()
            .toString()
    }

    @Benchmark
    fun presentations(): String {
        return rocker
            .presentations
            .template(presentations)
            .render()
            .toString()
    }
}