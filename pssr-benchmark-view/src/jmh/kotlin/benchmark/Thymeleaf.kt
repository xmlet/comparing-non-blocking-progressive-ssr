package benchmark

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.context.IContext
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.util.Locale

open class Thymeleaf : BaseBenchmark() {
    private lateinit var engine: TemplateEngine
    private lateinit var context: IContext

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)

        context = Context(Locale.getDefault(), getContext(stockRepository, presentationsRepository))

        engine = TemplateEngine().apply {
            val resolver =
                ClassLoaderTemplateResolver().apply {
                    prefix = "templates/thymeleaf/"
                    suffix = ".html"
                    characterEncoding = "UTF-8"
                }
            setTemplateResolver(resolver)
        }
    }

    @Benchmark
    fun stocks(): String {
        return engine.process("stocks-thymeleaf", context)
    }

    @Benchmark
    fun presentations(): String {
        return engine.process("index-thymeleaf", context)
    }
}