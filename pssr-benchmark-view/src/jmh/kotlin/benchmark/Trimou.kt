package benchmark

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import org.trimou.engine.MustacheEngineBuilder
import org.trimou.engine.config.EngineConfigurationKey.SKIP_VALUE_ESCAPING
import org.trimou.engine.locator.ClassPathTemplateLocator.builder
import org.trimou.engine.resolver.CombinedIndexResolver.ENABLED_KEY
import org.trimou.handlebars.BasicValueHelper
import org.trimou.handlebars.HelpersBuilder.extra
import org.trimou.handlebars.Options

open class Trimou : BaseBenchmark() {
    private lateinit var context : Map<String, Any>

    private lateinit var stocksTemplate: org.trimou.Mustache
    private lateinit var presentationsTemplate: org.trimou.Mustache

    @Setup
    fun setup() {
        val presentationsRepository = benchmark.repository.PresentationRepositoryMem(0)
        val stockRepository = benchmark.repository.StockRepositoryMem(0)

        context = getContext(stockRepository, presentationsRepository)

        val engine = MustacheEngineBuilder.newBuilder()
            .setProperty(SKIP_VALUE_ESCAPING, true)
            .setProperty(ENABLED_KEY, false)
            .addTemplateLocator(
                builder(1)
                    .setRootPath("templates/trimou/")
                    .setScanClasspath(false)
                    .setSuffix("trimou").build(),
            )
            .registerHelpers(extra().build())
            .registerHelper(
                "minusClass",
                object : BasicValueHelper() {
                    override fun execute(options: Options) {
                        val value = options.parameters[0]
                        if (value is Double && value < 0) {
                            options.append(" class=\"minus\"")
                        }
                    }
                },
            )
            .build()

        stocksTemplate = engine.getMustache("stocks")
        presentationsTemplate = engine.getMustache("presentations")
    }

    @Benchmark
    fun stocks(): String {
        return stocksTemplate.render(context)
    }

    @Benchmark
    fun presentations(): String {
        return presentationsTemplate.render(context)
    }
}