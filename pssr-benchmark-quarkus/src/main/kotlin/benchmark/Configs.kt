package benchmark

import benchmark.controller.presentations.DEFAULT_FREEMARKER_CONFIG
import benchmark.controller.presentations.DEFAULT_MUSTACHE_ENGINE
import benchmark.controller.presentations.DEFAULT_PEBBLE_ENGINE
import benchmark.controller.presentations.DEFAULT_THYMELEAF_ENGINE
import benchmark.controller.presentations.DEFAULT_VELOCITY_ENGINE
import benchmark.repository.PresentationRepository
import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepository
import benchmark.repository.StockRepositoryMem
import freemarker.template.Configuration
import io.pebbletemplates.pebble.PebbleEngine
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.ws.rs.ApplicationPath
import jakarta.ws.rs.core.Application
import org.apache.velocity.app.VelocityEngine
import org.thymeleaf.TemplateEngine
import org.trimou.engine.MustacheEngine

@ApplicationPath("/")
class JerseyConfig : Application()

@ApplicationScoped
class RepositoryConfig {
    private val timeout = System.getProperty("benchTimeout")?.toLongOrNull() ?: 100L

    @Produces
    fun presentationRepository(): PresentationRepository {
        return PresentationRepositoryMem(timeout)
    }

    @Produces
    fun stocksRepository(): StockRepository {
        return StockRepositoryMem(timeout)
    }
}

@ApplicationScoped
class TemplateEngineConfig {
    @Produces
    fun freemarkerConfig(): Configuration {
        return DEFAULT_FREEMARKER_CONFIG
    }

    @Produces
    fun pebbleEngine(): PebbleEngine {
        return DEFAULT_PEBBLE_ENGINE
    }

    @Produces
    fun thymeleafEngine(): TemplateEngine {
        return DEFAULT_THYMELEAF_ENGINE
    }

    @Produces
    fun mustacheEngine(): MustacheEngine {
        return DEFAULT_MUSTACHE_ENGINE
    }

    @Produces
    fun velocityEngine(): VelocityEngine {
        return DEFAULT_VELOCITY_ENGINE
    }
}
