package benchmark.webflux.config

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver

@Configuration
open class WebFluxConfig : ApplicationContextAware, WebFluxConfigurer {
    private var applicationContext: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(applicationContext!!.getBean<ThymeleafReactiveViewResolver?>(ThymeleafReactiveViewResolver::class.java))
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt")
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
    }

    @Bean
    open fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasename("classpath:/messages")
        messageSource.setDefaultEncoding("UTF-8")
        return messageSource
    }
}
