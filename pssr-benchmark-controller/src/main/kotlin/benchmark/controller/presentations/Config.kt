package benchmark.controller.presentations

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.DefaultObjectWrapperBuilder
import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.ClasspathLoader
import org.apache.velocity.app.VelocityEngine
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.trimou.engine.MustacheEngineBuilder
import org.trimou.engine.config.EngineConfigurationKey.SKIP_VALUE_ESCAPING
import org.trimou.engine.locator.ClassPathTemplateLocator.builder
import org.trimou.engine.resolver.CombinedIndexResolver.ENABLED_KEY
import org.trimou.handlebars.BasicValueHelper
import org.trimou.handlebars.HelpersBuilder.extra
import org.trimou.handlebars.Options
import java.util.Locale
import java.util.Properties

val DEFAULT_FREEMARKER_CONFIG =
    Configuration(Configuration.VERSION_2_3_32).apply {
        templateLoader = ClassTemplateLoader(javaClass, "/")
        defaultEncoding = "UTF-8"
        setSetting("cache_storage", "freemarker.cache.NullCacheStorage")
        setSetting("template_update_delay", "0")
        setSetting("locale", Locale.US.toString())
        setSetting("template_exception_handler", "rethrow")
        objectWrapper =
            DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_32)
                .apply { iterableSupport = true }
                .build()
    }

val DEFAULT_PEBBLE_ENGINE =
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

val DEFAULT_THYMELEAF_ENGINE =
    TemplateEngine().apply {
        val resolver =
            ClassLoaderTemplateResolver().apply {
                prefix = "templates/thymeleaf/"
                suffix = ".html"
                characterEncoding = "UTF-8"
                isCacheable = false
            }
        setTemplateResolver(resolver)
    }

val DEFAULT_MUSTACHE_ENGINE =
    MustacheEngineBuilder.newBuilder()
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

val DEFAULT_VELOCITY_ENGINE =
    Properties().apply {
        setProperty("resource.loaders", "class")
        setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        setProperty("resource.loader.class.path", "classpath:/templates/velocity/")
        setProperty("resource.loader.class.cache", "false")
        setProperty("view-names", "*-velocity")
        setProperty("toolboxConfigLocation", "/templates/velocity/toolbox.xml")
        setProperty("layout-enabled", "false")
        setProperty("cache", "false")
        setProperty("charset", "UTF-8")
    }.let { VelocityEngine(it) }
