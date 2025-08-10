import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.nims"
version =
    libs.versions.server.version
        .get()

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

extensions.configure<SpotlessExtension> {
    val buildDirectory = layout.buildDirectory.asFileTree
    kotlin {
        ktlint(libs.versions.ktlint.get())
        target("**/*.kt", "**/*.kts")
        targetExclude(buildDirectory)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

dependencies {
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
