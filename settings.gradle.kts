rootProject.name = "The Devil's Deal"
include(":server", ":client")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        id("io.ktor.plugin").version(extra["ktor.version"] as String)
        id("org.jetbrains.kotlin.plugin.serialization").version(extra["kotlin.version"] as String)
        id("com.github.node-gradle.node").version(extra["node-gradle.version"] as String)
    }
}
