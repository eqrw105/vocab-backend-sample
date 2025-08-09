import com.diffplug.gradle.spotless.SpotlessExtension

val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.3"
    id("com.diffplug.spotless") version "7.1.0"
}

group = "com.nims"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

extensions.configure<SpotlessExtension> {
    val buildDirectory = layout.buildDirectory.asFileTree
    kotlin {
        ktlint("1.7.1")
        target("**/*.kt", "**/*.kts")
        targetExclude(buildDirectory)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
