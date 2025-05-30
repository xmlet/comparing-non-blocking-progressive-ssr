package benchmark

import benchmark.controller.presentations.DEFAULT_FREEMARKER_CONFIG
import benchmark.controller.presentations.DEFAULT_MUSTACHE_ENGINE
import benchmark.controller.presentations.DEFAULT_PEBBLE_ENGINE
import benchmark.controller.presentations.DEFAULT_THYMELEAF_ENGINE
import benchmark.controller.presentations.DEFAULT_VELOCITY_ENGINE
import benchmark.repository.PresentationRepositoryMem
import benchmark.repository.StockRepositoryMem
import benchmark.webflux.router.PresentationsRoutes
import benchmark.webflux.router.StocksRoutes
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.codec.support.DefaultServerCodecConfigurer
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveViewResolver
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

// -i 4 iterations
// -wi 4 warmup iterations
// -f 1 fork
// -r 2 run each iteration for 2 seconds
// -w 2 run each warmup iteration for 2 seconds.
// -t 8 worker threads
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@org.openjdk.jmh.annotations.Fork(value = 1)
@org.openjdk.jmh.annotations.State(Scope.Benchmark) // java -jar target/template-engines.jar -i 4 -wi 4 -f 1 -r 2 -w 2 -t 8 -p route=/rocker/sync,/thymeleaf,/htmlFlow,/kotlinx
open class LaunchJMH {
    @Param(
//        "/presentations/thymeleaf/sync",
//        "/presentations/htmlFlow/sync",
//        "/presentations/htmlFlow/suspending",
//        "/presentations/kotlinx",
//        "/presentations/rocker/sync",
//        "/presentations/jstachio/sync",
//        "/presentations/freemarker/sync",
//        "/presentations/trimou/sync",
//        "/stocks/thymeleaf/sync",
        "/stocks/htmlFlow/sync",
        "/stocks/htmlFlow",
        "/stocks/htmlFlow/virtualSync",
//        "/stocks/kotlinx",
//        "/stocks/rocker/sync",
        "/stocks/jstachio/sync",
        "/stocks/jstachio/virtualSync",
//        "/stocks/freemarker/sync",
//        "/stocks/trimou/sync",
    )
    lateinit var route: String

    private val timeout = 0L // We are only evaluating the rendering speed of each template engine, no need for context switching
    private val presentationsRepositoryMem = PresentationRepositoryMem(timeout)
    private val stocksRepositoryMem = StockRepositoryMem(timeout)

    private val presentationsRouter =
        PresentationsRoutes(
            freemarkerConfig = DEFAULT_FREEMARKER_CONFIG,
            pebbleEngine = DEFAULT_PEBBLE_ENGINE,
            thymeleafEngine = DEFAULT_THYMELEAF_ENGINE,
            mustacheEngine = DEFAULT_MUSTACHE_ENGINE,
            velocityEngine = DEFAULT_VELOCITY_ENGINE,
            presentations = presentationsRepositoryMem,
        )

    private val stocksRouter =
        StocksRoutes(
            freemarkerConfig = DEFAULT_FREEMARKER_CONFIG,
            pebbleEngine = DEFAULT_PEBBLE_ENGINE,
            thymeleafEngine = DEFAULT_THYMELEAF_ENGINE,
            mustacheEngine = DEFAULT_MUSTACHE_ENGINE,
            velocityEngine = DEFAULT_VELOCITY_ENGINE,
            stocks = stocksRepositoryMem,
        )

    val context =
        object : ServerResponse.Context {
            override fun messageWriters() = DefaultServerCodecConfigurer().writers

            override fun viewResolvers() = listOf(ThymeleafReactiveViewResolver())
        }

    val readers: List<HttpMessageReader<*>?> = DefaultServerCodecConfigurer().readers

    val presentationRouter = presentationsRouter.presentationsRouter()
    val stockRouter = stocksRouter.stocksRouter()

    @Benchmark
    fun benchRoute(): String {
        val router =
            when {
                route.startsWith("/presentations") -> presentationRouter
                route.startsWith("/stocks") -> stockRouter
                else -> throw IllegalArgumentException("Unknown route: $route")
            }

        val request = MockServerHttpRequest.get(route).build()
        val exchange = MockServerWebExchange.from(request)
        val serverRequest = ServerRequest.create(exchange, readers)

        val response =
            router.route(serverRequest)
                .flatMap { it.handle(serverRequest) }
                .flatMap { it.writeTo(exchange, context) }
                .then(
                    Mono.defer {
                        exchange.response.body
                            .reduce(StringBuilder()) { builder, buffer ->
                                val bytes = ByteArray(buffer.readableByteCount())
                                buffer.read(bytes)
                                builder.append(String(bytes, Charsets.UTF_8))
                            }
                            .map { it.toString() }
                    },
                )
                .block()

        return response ?: throw IllegalStateException("Response body is null")
    }
}
