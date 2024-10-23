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
    implementation(project(":app-common"))

    testImplementation(libs.test.junit.jupiter)
    testRuntimeOnly(libs.test.junit.platform)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.mockk)
}
