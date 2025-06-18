package benchmark

import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepositoryMem
import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.ClasspathLoader
import io.pebbletemplates.pebble.template.PebbleTemplate
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.StringWriter

open class Pebble : BaseBenchmark() {
    private lateinit var stocksTemplate: PebbleTemplate
    private lateinit var presentationsTemplate: PebbleTemplate
    private lateinit var context: Map<String, Any>

    @Setup
    fun setup() {
        val presentationsRepository = PresentationRepositoryMem(0)
        val stockRepository = StockRepositoryMem(0)

        context = getContext(stockRepository, presentationsRepository)

        val pebbleEngine =
            PebbleEngine.Builder()
                .loader(
                    ClasspathLoader().apply {
                        prefix = "templates/pebble/"
                        suffix = ".pebble.html"
                        charset = "UTF-8"
                    },
                )
                .cacheActive(false)
                .autoEscaping(false)
                .build()
        stocksTemplate = pebbleEngine.getTemplate("stocks")
        presentationsTemplate = pebbleEngine.getTemplate("presentations")
    }

    @Benchmark
    fun stocks(): String {
        val writer = StringWriter()
        stocksTemplate.evaluate(writer, context)
        return writer.toString()
    }

    @Benchmark
    fun presentations(): String {
        val writer = StringWriter()
        presentationsTemplate.evaluate(writer, context)
        return writer.toString()
    }
}