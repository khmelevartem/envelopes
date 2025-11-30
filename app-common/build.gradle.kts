plugins {
    `java-library`
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(libs.koin.core)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.datetime)

    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.platform)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.mockk)
}
