plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("nu.studer.rocker") version "3.0.5"
    id("me.champeau.jmh") version "0.7.3"
}

group = "pt.isel.pfc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.fizzed:rocker-runtime:2.2.1")

    api("io.jstach:jstachio:1.3.7")
    kapt("io.jstach:jstachio-apt:1.3.7")

    api("io.pebbletemplates:pebble:3.2.4")
    api("org.freemarker:freemarker:2.3.34")
    api("org.trimou:trimou-core:2.5.1.Final")
    api("org.apache.velocity:velocity-engine-core:2.3")

    api("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
    api("com.github.xmlet:htmlflow:4.7")
    api("org.jetbrains.kotlin:kotlin-stdlib:2.0.10")
    api("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    api("io.projectreactor:reactor-core:3.7.4")

    implementation(project(":pssr-benchmark-model"))
    implementation("io.reactivex.rxjava3:rxjava:3.1.10")

    implementation("org.openjdk.jmh:jmh-core:1.35")
    implementation("org.openjdk.jmh:jmh-core-benchmarks:1.35")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")

    // For JHM benches
    implementation(project(":pssr-benchmark-repository"))
    implementation(project(":pssr-benchmark-repository-mem"))

    api("org.webjars:bootstrap:5.3.0")
    api("org.webjars:jquery:3.6.4")

    testImplementation(kotlin("test"))
}

tasks.register<Exec>("benchJMHPresentations") {
    dependsOn("jmhJar")
    commandLine(
        "java",
        "-jar",
        "${layout.buildDirectory.get().asFile}/libs/pssr-benchmark-view-${version}-jmh.jar",
        "-i", "4", // iterations
        "-wi", "4", // warmup iterations
        "-f", "1", // forks
        "-r", "2s", // time for each iteration
        "-w", "2s", // warmup time
        "-t", "8", // threads
        "-rff", "${layout.projectDirectory.asFile.parentFile}/results/results-jmh-presentations.csv",
        "-rf", "csv", // result format
        "-tu", "ms", // time unit
        "presentations"
    )
}

tasks.register<Exec>("benchJMHStocks") {
    dependsOn("jmhJar")
    commandLine(
        "java",
        "-jar",
        "${layout.buildDirectory.get().asFile}/libs/pssr-benchmark-view-${version}-jmh.jar",
        "-i", "4", // iterations
        "-wi", "4", // warmup iterations
        "-f", "1", // forks
        "-r", "2s", // time for each iteration
        "-w", "2s", // warmup time
        "-t", "8", // threads
        "-rff", "${layout.projectDirectory.asFile.parentFile}/results/results-jmh-stocks.csv",
        "-rf", "csv", // result format
        "-tu", "ms", // time unit
        "stocks"
    )
}

rocker {
    configurations {
        create("main") {
            templateDir = file("src/main/resources/templates")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
