pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "kotlinx-serialization-android-benchmarks"
