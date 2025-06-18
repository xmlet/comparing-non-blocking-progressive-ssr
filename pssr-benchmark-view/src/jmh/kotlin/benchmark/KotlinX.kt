package benchmark

import benchmark.model.Presentation
import benchmark.model.Stock
import benchmark.view.presentations.PresentationsKotlinX
import benchmark.view.stocks.StocksKotlinX
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.StringWriter
import kotlin.test.assertEquals

open class KotlinX : BaseBenchmark() {
    private lateinit var stocks: Iterable<Stock>
    private lateinit var presentations: Iterable<Presentation>

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)

        stocks = stockRepository.findAllIterable()
        presentations = presentationsRepository.findAllIterable()
    }

    @Benchmark
    fun stocks(): String {
        val writer = StringWriter()
        StocksKotlinX.kotlinXIter(writer, stocks)
        return writer.toString()
    }

    @Benchmark
    fun presentations(): String {
        val writer = StringWriter()
        PresentationsKotlinX.kotlinXIter(writer, presentations)
        return writer.toString()
    }

    override fun validateStocks(response: String) {
        val expected = htmlResponseStocks.replace("<!DOCTYPE html>", "")
        assertEquals(trimLines(expected), trimLines(response))
    }

    override fun validatePresentations(response: String) {
        val expected = htmlResponsePresentations.replace("<!DOCTYPE html>", "")
        assertEquals(trimLines(expected), trimLines(response))
    }
}