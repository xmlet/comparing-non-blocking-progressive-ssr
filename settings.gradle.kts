plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "pssr-benchmark"

include("pssr-benchmark-model")
include("pssr-benchmark-repository")
include("pssr-benchmark-repository-mem")
include("pssr-benchmark-view")
include("pssr-benchmark-controller")
include("pssr-benchmark-spring-webflux")
include("pssr-benchmark-spring-mvc")
include("pssr-benchmark-controller")
include("pssr-benchmark-quarkus")
