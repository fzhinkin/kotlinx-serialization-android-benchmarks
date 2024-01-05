plugins {
    id("org.jetbrains.kotlin.android") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("androidx.benchmark") version "1.1.1"
    id("com.android.library") version "8.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    mavenLocal()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-kotlinx-io:1.6.3-SNAPSHOT")
    // androidTestImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json-kotlinx-io:1.6.3-SNAPSHOT")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.1-SNAPSHOT")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.benchmark:benchmark-junit4:1.1.1")
}


kotlin {
    jvmToolchain(8)
}

android {
    namespace = "kotlinx.io.benchmark.android"
    compileSdk = 33

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.output.enable"] = "true"
    }

    testBuildType = "release"
    buildTypes {
        debug {
            // Since isDebuggable can"t be modified by gradle for library modules,
            // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "benchmark-proguard-rules.pro")
        }
        release {
            isDefault = true
        }
    }
}
