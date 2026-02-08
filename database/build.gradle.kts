plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    macosArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":app-common"))
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

room {
    schemaDirectory(layout.projectDirectory.dir("schemas").toString())
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspMacosArm64", libs.androidx.room.compiler)
}

android {
    namespace = "com.tubetoast.envelopes.database"
    compileSdk = libs.versions.androidCompileSdk
        .get()
        .toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk
            .get()
            .toInt()
    }
}
