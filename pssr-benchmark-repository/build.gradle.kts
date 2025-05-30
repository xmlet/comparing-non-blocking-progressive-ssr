plugins {
    kotlin("jvm")
}

group = "pt.isel.pfc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":pssr-benchmark-model"))
    api("io.reactivex.rxjava3:rxjava:3.1.10")
    // tests
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
