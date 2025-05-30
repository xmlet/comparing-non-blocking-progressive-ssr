plugins {
    kotlin("jvm")
    id("org.kordamp.gradle.jandex") version "1.0.0"
}

group = "pt.isel.pfc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.cxf:cxf-rt-rs-extension-reactor:4.1.1")
    implementation("org.apache.cxf:cxf-spring-boot-starter-jaxrs:4.1.1")

    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")

    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:3.15.4"))
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkiverse.qute.web:quarkus-qute-web")

    implementation(project(":pssr-benchmark-repository"))
    implementation(project(":pssr-benchmark-repository-mem"))
    implementation(project(":pssr-benchmark-view"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
