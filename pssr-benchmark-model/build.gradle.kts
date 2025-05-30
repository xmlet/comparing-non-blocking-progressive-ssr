plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}
