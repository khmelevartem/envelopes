plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tubetoast.envelopes"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes.getByName("release") {
        minifyEnabled(false)
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
    }
    kotlin {
        jvmToolchain(21)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/**"
            excludes += "DebugProbesKt.bin"
        }
    }
    namespace = "com.tubetoast.envelopes"
}

dependencies {

    // Project
    implementation(project(":app-common"))
    implementation(project(":monefy-parser"))
    implementation(project(":database"))

    // DI
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.icons)
    implementation(libs.compose.preview)
    implementation(libs.compose.navigation)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)

    implementation(libs.vico.compose.m3)

    // Kotlinx
    implementation(libs.kotlinx.serialization)

    // test
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.mockk)
}
