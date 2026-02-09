plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotest)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            api(libs.koin.core)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotest.assertions)
            implementation(libs.kotest.framework.engine)
        }
        androidUnitTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }
}

android {
    namespace = "com.tubetoast.envelopes.monefy"
    compileSdk = libs.versions.androidCompileSdk
        .get()
        .toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk
            .get()
            .toInt()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
