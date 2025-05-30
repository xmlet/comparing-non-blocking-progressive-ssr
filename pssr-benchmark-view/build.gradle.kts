plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("nu.studer.rocker") version "3.0.5"
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

    api("org.webjars:bootstrap:5.3.0")
    api("org.webjars:jquery:3.6.4")

    testImplementation(kotlin("test"))
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
