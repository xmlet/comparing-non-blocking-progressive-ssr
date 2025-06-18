package benchmark

import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepositoryMem
import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.DefaultObjectWrapperBuilder
import freemarker.template.Template
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.StringWriter
import java.util.Locale

open class Freemarker: BaseBenchmark() {
    private lateinit var stocksTemplate: Template
    private lateinit var presentationsTemplate: Template
    private lateinit var context: Map<String, Any>

    @Setup
    fun setup() {
        val presentationsRepository = PresentationRepositoryMem(0)
        val stockRepository = StockRepositoryMem(0)

        context = getContext(stockRepository, presentationsRepository)

        val freemarkerEngine =
            Configuration(Configuration.VERSION_2_3_32).apply {
                templateLoader = ClassTemplateLoader(javaClass, "/")
                defaultEncoding = "UTF-8"
                setSetting("template_update_delay", "0")
                setSetting("locale", Locale.US.toString())
                setSetting("template_exception_handler", "rethrow")
                objectWrapper =
                    DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32)
                        .apply { iterableSupport = true }
                        .build()
            }

        stocksTemplate = freemarkerEngine.getTemplate("templates/freemarker/stocks-freemarker.ftl")
        presentationsTemplate = freemarkerEngine.getTemplate("templates/freemarker/index-freemarker.ftl")
    }

    @Benchmark
    fun stocks(): String {
        val writer = StringWriter()
        stocksTemplate.process(context, writer)
        return writer.toString()
    }

    @Benchmark
    fun presentations(): String {
        val writer = StringWriter()
        presentationsTemplate.process(context, writer)
        return writer.toString()
    }
}