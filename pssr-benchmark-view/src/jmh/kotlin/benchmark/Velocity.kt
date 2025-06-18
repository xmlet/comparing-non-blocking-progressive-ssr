package benchmark

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.StringWriter
import java.util.Properties

open class Velocity : BaseBenchmark() {
    private lateinit var context: VelocityContext

    private lateinit var stocksTemplate: Template
    private lateinit var presentationsTemplate: Template

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)

        context = VelocityContext(getContext(stockRepository, presentationsRepository))

        val engine = Properties().apply {
            setProperty("resource.loaders", "class")
            setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
            setProperty("resource.loader.class.path", "classpath:/templates/velocity/")
            setProperty("resource.loader.class.cache", "false")
            setProperty("view-names", "*-velocity")
            setProperty("layout-enabled", "false")
            setProperty("cache", "false")
            setProperty("charset", "UTF-8")
        }.let { VelocityEngine(it) }

        stocksTemplate = engine.getTemplate("templates/velocity/stocks-velocity.vm")
        presentationsTemplate = engine.getTemplate("templates/velocity/presentations-velocity.vm")
    }

    @Benchmark
    fun stocks(): String {
        val writer = StringWriter()
        stocksTemplate.merge(context, writer)
        return writer.toString()
    }

    @Benchmark
    fun presentations(): String {
        val writer = StringWriter()
        presentationsTemplate.merge(context, writer)
        return writer.toString()
    }
}