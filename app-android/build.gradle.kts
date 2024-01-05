plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 34

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }
    packagingOptions {
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

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.preview)
    implementation(libs.compose.navigation)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)

    // test
    testImplementation(libs.test.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.mockk)
}