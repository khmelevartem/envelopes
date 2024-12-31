/*
 * Copyright (c) New Cloud Technologies, Ltd., 2013-2024
 *
 * You can not use the contents of the file in any way without New Cloud Technologies, Ltd. written permission.
 * To obtain such a permit, you should contact New Cloud Technologies, Ltd. at https://myoffice.ru/contacts/
 *
 */

plugins {
    id("com.android.test")
    kotlin("android")
}

android {
    namespace = "com.tubetoast.envelopes.benchmark"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }

    targetProjectPath = ":app-android"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    compileOnly("androidx.benchmark:benchmark-common:1.2.3") {
        because("to look into lib's sources")
    }
    implementation(libs.androidx.benchmark)
    implementation(libs.test.androidxTestExt)
    implementation(libs.test.espressoCore)
    implementation(libs.test.uiautomator)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
