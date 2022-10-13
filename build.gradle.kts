import com.github.gradle.node.npm.task.NpmTask

val nodeVersion = extra["node.version"] as String

plugins {
    kotlin("jvm")
    id("com.github.node-gradle.node")
}

group = "com.notkamui"
version = extra["project.version"] as String

repositories {
    mavenCentral()
}

node {
    version.set(nodeVersion)
    distBaseUrl.set("https://nodejs.org/dist")
    download.set(true)
    nodeProjectDir.set(file("${project.projectDir}/client"))
    workDir.set(file("${project.projectDir}/.gradle/nodejs"))
}

tasks {
    val npmClean = register<Delete>("npmClean") {
        delete(file("${project.projectDir}/client/node_modules"))
        delete(file("${project.projectDir}/client/dist"))
        delete(file("${project.projectDir}/server/src/main/resources/static"))
    }

    val npmBuild = register<NpmTask>("npmBuild") {
        dependsOn(npmInstall)
        npmCommand.set(listOf("run", "build"))
        ignoreExitValue.set(false)
    }

    val bundleClient = register<Copy>("bundleClient") {
        dependsOn(npmBuild)
        from("client/dist")
        into("server/src/main/resources/static")
    }

    clean {
        dependsOn(npmClean)
        delete(file("executables"))
    }

    build {
        dependsOn(bundleClient)
    }
}
