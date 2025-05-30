package benchmark.webflux.config

import benchmark.controller.presentations.DEFAULT_FREEMARKER_CONFIG
import benchmark.controller.presentations.DEFAULT_MUSTACHE_ENGINE
import benchmark.controller.presentations.DEFAULT_PEBBLE_ENGINE
import benchmark.controller.presentations.DEFAULT_THYMELEAF_ENGINE
import benchmark.controller.presentations.DEFAULT_VELOCITY_ENGINE
import io.pebbletemplates.pebble.PebbleEngine
import org.apache.velocity.app.VelocityEngine
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.trimou.engine.MustacheEngine

@Configuration
open class TemplateEngineConfig {
    @Bean
    open fun freemarkerConfig(): freemarker.template.Configuration {
        return DEFAULT_FREEMARKER_CONFIG
    }

    @Bean
    open fun pebbleEngine(): PebbleEngine {
        return DEFAULT_PEBBLE_ENGINE
    }

    @Bean
    open fun thymeleafEngine(): TemplateEngine {
        return DEFAULT_THYMELEAF_ENGINE
    }

    @Bean
    open fun mustacheEngine(): MustacheEngine {
        return DEFAULT_MUSTACHE_ENGINE
    }

    @Bean
    open fun velocityEngine(): VelocityEngine {
        return DEFAULT_VELOCITY_ENGINE
    }
}
