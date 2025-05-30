import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.quarkus") version "3.15.4"
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("jvm")
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

    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:3.15.4"))

    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")

    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test"))
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

configurations.all {
    exclude(group = "org.jboss.slf4j", module = "slf4j-jboss-logmanager")
}

tasks.register<JavaExec>("runQuarkus") {
    dependsOn(tasks.named("quarkusBuild"))
    mainClass.set("benchmark.LaunchKt")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs =
        listOf(
            "-Dquarkus.virtual-threads.enabled=false",
        )
    systemProperties = System.getProperties().entries.associate { (k, v) -> k.toString() to v.toString() }
}

tasks.register<JavaExec>("runQuarkusVirtual") {
    dependsOn(tasks.named("quarkusBuild"))
    mainClass.set("benchmark.LaunchKt")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs =
        listOf(
            "-Dquarkus.virtual-threads.enabled=true",
            "-Djdk.tracePinnedThreads",
        )
    systemProperties = System.getProperties().entries.associate { (k, v) -> k.toString() to v.toString() }
}

tasks.named("quarkusDependenciesBuild") {
    dependsOn(tasks.named("jandex"))
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
