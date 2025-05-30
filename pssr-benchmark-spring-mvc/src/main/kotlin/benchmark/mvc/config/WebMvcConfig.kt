package benchmark.mvc.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebMvcConfig : ApplicationContextAware, WebMvcConfigurer {
    private var applicationContext: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt")
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
    }

    @Bean
    open fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        return executor
    }

    @Bean
    open fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:/messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}
