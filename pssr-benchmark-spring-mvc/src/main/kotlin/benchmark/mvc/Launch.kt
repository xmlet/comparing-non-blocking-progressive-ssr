package benchmark.mvc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Launch {
//    companion object {
//        private val logger = LoggerFactory.getLogger(Launch::class.java)
//    }
//
//    @Bean
//    open fun beanPostProcessor() =
//        object : BeanPostProcessor {
//            override fun postProcessAfterInitialization(
//                bean: Any,
//                beanName: String,
//            ): Any {
//                val beanPackage = bean::class.java.packageName
//                if (beanPackage.startsWith("benchmark")) {
//                    logger.info("Bean $beanName of package $beanPackage initialized")
//                }
//                return bean
//            }
//        }
}

fun main(args: Array<String>) {
    SpringApplication.run(Launch::class.java, *args)
}
