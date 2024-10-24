plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tubetoast.envelopes.database"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
    }

    buildTypes.getByName("release") {
        minifyEnabled(false)
    }
    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    implementation(project(":app-common"))
    implementation(libs.koin.android)
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}
