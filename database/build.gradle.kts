plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":app-common"))
    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
}