plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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
