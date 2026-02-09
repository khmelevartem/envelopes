plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    macosArm64()
    jvmToolchain(21)

    macosArm64 {
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(project(":database"))
            implementation(project(":monefy-parser"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.navigation)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.vico.compose.m3)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.tubetoast.envelopes"
    compileSdk = libs.versions.androidCompileSdk
        .get()
        .toInt()
    defaultConfig {
        applicationId = "com.tubetoast.envelopes"
        minSdk = libs.versions.androidMinSdk
            .get()
            .toInt()
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes.getByName("release") {
        minifyEnabled(false)
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/**"
            excludes += "DebugProbesKt.bin"
        }
    }
    namespace = "com.tubetoast.envelopes"
}
