val jvmVersion = extra["jvm.version"] as String
val kotlinVersion = extra["kotlin.version"] as String
val ktorVersion = extra["ktor.version"] as String
val logbackVersion = extra["logback.version"] as String

plugins {
    application
    java
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.notkamui"
version = extra["project.version"] as String

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor", "ktor-server-core-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-auth-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-host-common-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-status-pages-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-cors-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-content-negotiation-jvm", ktorVersion)
    implementation("io.ktor", "ktor-serialization-kotlinx-json-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-netty-jvm", ktorVersion)

    implementation("ch.qos.logback", "logback-classic", logbackVersion)

    testImplementation("io.ktor", "ktor-server-tests- org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType.jvm", ktorVersion)
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit", kotlinVersion)
}

tasks {
    compileJava {
        sourceCompatibility = jvmVersion
        targetCompatibility = jvmVersion
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = jvmVersion
            freeCompilerArgs = listOf(
                "-Xjvm-default=all",
                "-Xlambdas=indy",
                "-Xsam-conversions=indy",
            )
        }
    }

    shadowJar {
        destinationDirectory.set(file("$rootDir/executables"))
    }
}

ktor.fatJar.archiveFileName.set("${rootProject.name}.jar")
