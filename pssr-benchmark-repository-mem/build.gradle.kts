plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":pssr-benchmark-repository"))
    // testing
    testImplementation(kotlin("test"))
}
