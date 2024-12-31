pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Envelopes"
include(":app-android")
include(":app-common")
include(":monefy-parser")
include(":database")
include(":benchmark")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.5.0")
}
