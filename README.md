
# Comparing Non Blocking Progressive Server-Side Rendering (PSSR) 

This project builds upon previous work comparing Java template engines for [Spring WebFlux](https://github.com/xmlet/spring-webflux-comparing-template-engines) and [Spring MVC](https://github.com/jreijn/spring-comparing-template-engines), originally presented in ["Shoot-out! Template engines for the JVM"](https://www.slideshare.net/slideshow/comparing-templateenginesjvm/27994062) by [Jeroen Reijn](https://github.com/jreijn).

We benchmark multiple Java template engines across three frameworks — **Quarkus**, **Spring MVC**, and **Spring WebFlux**, focusing on **Progressive Server-Side Rendering (PSSR)**, scalability under load, and modern Java features like **virtual threads**.

---

## Template Engines

The following engines are included in the benchmark:

| Engine                                                 | Version     |
|--------------------------------------------------------|-------------|
| [Thymeleaf](https://www.thymeleaf.org/)                | 3.1.3       |
| [HtmlFlow](https://htmlflow.org/)                      | 4.7         |
| [kotlinx.html](https://github.com/Kotlin/kotlinx.html) | 0.12.0      |
| [Rocker](https://github.com/fizzed/rocker)             | 2.2.1       |
| [JStachio](https://jstach.io/jstachio/)                | 1.3.7       |
| [Pebble](https://pebbletemplates.io/)                  | 3.2.4       |
| [Freemarker](https://freemarker.apache.org/)           | 2.3.34      |
| [Trimou](https://github.com/trimou/trimou)             | 2.5.1.Final |
| [Velocity](https://velocity.apache.org/)               | 2.3         |

---

## Changes from Previous Work

- Added **Quarkus** and **Spring MVC** support, alongside Spring WebFlux.
- Integrated **virtual threads**, enabling non-blocking behavior in traditionally blocking engines.
- Replaced `Dispatchers.Default` with `Dispatchers.IO` (unlimited parallelism) in Spring WebFlux to measure blocking overhead.
- Enabled **progressive rendering** for engines without asynchronous data model support using `Observable.blockingIterable()`.

---

## Progressive Rendering Support

| Framework          | Supported    | Notes                                                                                                                                                    |
|--------------------|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Spring WebFlux** | ✅            | Uses `Flux<String>` and reactive adapters.                                                                                                               |
| **Spring MVC**     | ⚠️ Partially | `StreamingResponseBody` supported, but early buffering prevents complete streaming.                                                                      |
| **Quarkus**        | ✅            | Uses `Multi<String>` for reactive routes and StreamingOutput with configurable output buffer (`quarkus.rest.output-buffer-size`) for synchronous routes. |

---

## Available Routes

Each framework exposes routes under `/presentations` and `/stocks`, rendered by different engines and threading models.

<details>
<summary><strong> Spring WebFlux Routes</strong></summary>

**Presentations:**

- `/presentations/thymeleaf` - reactive 
- `/presentations/thymeleaf/sync` - synchronous, blocking
- `/presentations/thymeleaf/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/htmlFlow` - reactive
- `/presentations/htmlFlow/suspending` - suspending, non-blocking
- `/presentations/htmlFlow/sync` - synchronous, blocking
- `/presentations/htmlFlow/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/kotlinx` - reactive (malformed HTML)
- `/presentations/kotlinx/sync` - synchronous, blocking
- `/presentations/kotlinx/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/rocker/sync` - synchronous, blocking
- `/presentations/rocker/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/jstachio/sync` - synchronous, blocking
- `/presentations/jstachio/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/pebble/sync` - synchronous, blocking
- `/presentations/pebble/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/freemarker/sync` - synchronous, blocking
- `/presentations/freemarker/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/trimou/sync` - synchronous, blocking
- `/presentations/trimou/virtualSync` - synchronous, non-blocking with virtual threads
- `/presentations/velocity/sync` - synchronous, blocking
- `/presentations/velocity/virtualSync` - synchronous, non-blocking with virtual threads

**Stocks:**

- `/stocks/thymeleaf` - reactive
- `/stocks/thymeleaf/sync` - synchronous, blocking
- `/stocks/thymeleaf/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/htmlFlow` - reactive
- `/stocks/htmlFlow/suspending` - suspending, non-blocking
- `/stocks/htmlFlow/sync` - synchronous, blocking
- `/stocks/htmlFlow/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/kotlinx` - reactive (malformed HTML)
- `/stocks/kotlinx/sync` - synchronous, blocking
- `/stocks/kotlinx/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/rocker/sync` - synchronous, blocking
- `/stocks/rocker/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/jstachio/sync` - synchronous, blocking
- `/stocks/jstachio/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/pebble/sync` - synchronous, blocking
- `/stocks/pebble/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/freemarker/sync` - synchronous, blocking
- `/stocks/freemarker/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/trimou/sync` - synchronous, blocking
- `/stocks/trimou/virtualSync` - synchronous, non-blocking with virtual threads
- `/stocks/velocity/sync` - synchronous, blocking
- `/stocks/velocity/virtualSync` - synchronous, non-blocking with virtual threads

</details>

<details>
<summary><strong> Spring MVC Routes</strong></summary>

All routes are **blocking**, with optional support for **virtual threads** if the
`spring.threads.virtual.enabled` property is set to `true`.

**Presentations:**

- `/presentations/thymeleaf`
- `/presentations/htmlFlow`
- `/presentations/kotlinx`
- `/presentations/rocker`
- `/presentations/jstachio`
- `/presentations/pebble`
- `/presentations/freemarker`
- `/presentations/trimou`
- `/presentations/velocity`

**Stocks:**

- `/stocks/thymeleaf`
- `/stocks/htmlFlow`
- `/stocks/kotlinx`
- `/stocks/rocker`
- `/stocks/jstachio`
- `/stocks/pebble`
- `/stocks/freemarker`
- `/stocks/trimou`
- `/stocks/velocity`
- 
</details>

<details>
<summary><strong> Quarkus Routes</strong></summary>

All engines are **blocking** and support **virtual threads** if the
`quarkus.threads.virtual.enabled` property is set to `true`. Also 
includes a **reactive** route with `HtmlFlow`.

**Presentations:**

- `/presentations/thymeleaf`
- `/presentations/htmlFlow`
- `/presentations/kotlinx`
- `/presentations/rocker`
- `/presentations/jstachio`
- `/presentations/pebble`
- `/presentations/freemarker`
- `/presentations/trimou`
- `/presentations/velocity`
- `/presentations/reactive/htmlFlow`

**Stocks:**

- `/stocks/thymeleaf`
- `/stocks/htmlFlow`
- `/stocks/kotlinx`
- `/stocks/rocker`
- `/stocks/jstachio`
- `/stocks/pebble`
- `/stocks/freemarker`
- `/stocks/trimou`
- `/stocks/velocity`
- `/stocks/reactive/htmlFlow`

</details>

---

## Build & Run

### Prerequisites

- **Java 21**
- **Gradle 7+**

### Build All Modules

```bash
./gradlew build
```

### Run by Framework

#### Spring WebFlux

```bash
# JAR
java -jar pssr-benchmark-spring-webflux/build/libs/pssr-benchmark-spring-webflux-1.0-SNAPSHOT.jar

# Gradle
./gradlew runWebflux
```

#### Spring MVC

```bash
# JAR
java -jar pssr-benchmark-spring-mvc/build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT.jar

# Gradle
./gradlew runMVC           # Traditional threads
./gradlew runMVCVirtual    # Virtual threads
```

#### Quarkus

```bash
# JAR
java -jar pssr-benchmark-quarkus/build/quarkus-app/quarkus-run.jar

# Gradle
./gradlew runQuarkus        # Traditional threads
./gradlew runQuarkusVirtual # Virtual threads
```

---

## Running Benchmarks

### Apache Benchmark (ab)

```bash
cd benches/ab
./bench-webflux.sh
./bench-mvc.sh
./bench-quarkus.sh
```

### JMeter

```bash
cd benches/jmeter
./bench-webflux.sh
./bench-mvc.sh
./bench-quarkus.sh
```

### JMH (Java Microbenchmark Harness)

```bash
cd benches/jmh
./bench-jmh.sh
```

---

## Benchmark Results

All tests were run on a GitHub-hosted Ubuntu 22.04 VM with **4 cores**, **16GB RAM**, and **OpenJDK 21**.

### Performance (JMH)

The performance results show the throughput (number of renders per second) for each template engine. The goal
is to measure how well each engine performs in template rendering.

| Presentations                                                      | Stocks                                               |
|--------------------------------------------------------------------|------------------------------------------------------|
| ![Presentations Results](docs/plots/jmh_results_presentations.png) | ![Stocks Results](docs/plots/jmh_results_stocks.png) |

---

### Scalability

The scalability results depict the throughput (number of requests per second) for each template engine,
with concurrent requests ranging from 1 to 2500 users. The goal is to observe how well each engine/approach scales with increasing
server load. To simulate I/O operations and enable progressive rendering, we interleaved `Observable<T>` elements with
a 5-millisecond delay in each route.

#### Spring WebFlux Highlights

- `HtmlFlow-Susp` (suspendable templates)
- `Thymeleaf-Rx` (reactive)
- `Jstachio-Virtual` (virtual threads)
- Aggregated: `Blocking`, `Virtual`

| Presentations                                                  | Stocks                                           |
|----------------------------------------------------------------|--------------------------------------------------|
| ![WebFlux Presentations](docs/plots/presentations-webflux.png) | ![WebFlux Stocks](docs/plots/stocks-webflux.png) |

#### Spring MVC Highlights

- Aggregated: `Blocking`, `Virtual`

| Presentations                                          | Stocks                                   |
|--------------------------------------------------------|------------------------------------------|
| ![MVC Presentations](docs/plots/presentations-mvc.png) | ![MVC Stocks](docs/plots/stocks-mvc.png) |

#### Quarkus Highlights

- `HtmlFlow-Rx` (reactive)
- Aggregated: `Blocking`, `Virtual`

| Presentations                                                  | Stocks                                           |
|----------------------------------------------------------------|--------------------------------------------------|
| ![Quarkus Presentations](docs/plots/presentations-quarkus.png) | ![Quarkus Stocks](docs/plots/stocks-quarkus.png) |
