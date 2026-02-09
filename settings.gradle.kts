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
include(":application")
include(":core")
include(":monefy-parser")
include(":database")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.5.0")
}
