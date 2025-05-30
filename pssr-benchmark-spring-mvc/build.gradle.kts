import org.gradle.kotlin.dsl.testImplementation

plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("me.champeau.jmh") version "0.7.3"
}

group = "pt.isel.pfc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pssr-benchmark-controller"))
    implementation(project(":pssr-benchmark-view"))
    implementation(project(":pssr-benchmark-repository"))
    implementation(project(":pssr-benchmark-repository-mem"))

    implementation("org.springframework.boot:spring-boot-starter-jersey")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.openjdk.jmh:jmh-core:1.35")
    implementation("org.openjdk.jmh:jmh-core-benchmarks:1.35")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("org.mockito:mockito-core:5.10.0")
    testRuntimeOnly("org.mockito:mockito-inline:5.2.0")
}

configurations.all {
    exclude(group = "org.jboss.slf4j", module = "slf4j-jboss-logmanager")
}

tasks.register<JavaExec>("runMVC") {
    mainClass.set("benchmark.mvc.LaunchKt")
    classpath = sourceSets["main"].runtimeClasspath
    systemProperties = System.getProperties().entries.associate { (k, v) -> k.toString() to v.toString() }
}

tasks.register<JavaExec>("runMVCVirtual") {
    mainClass.set("benchmark.mvc.LaunchKt")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs =
        listOf(
            "-Dspring.threads.virtual.enabled=true",
            "-Djdk.tracePinnedThreads",
        )
    systemProperties = System.getProperties().entries.associate { (k, v) -> k.toString() to v.toString() }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
