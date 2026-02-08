import org.gradle.kotlin.dsl.ktlintRuleset
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotest) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.room) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        filter {
            exclude { element -> element.file.path.contains("generated/") }
            exclude { element -> element.file.path.contains("build/") }
            exclude("**/generated/**")
            exclude("**/build/**")
        }

        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }
}

dependencies {
    ktlintRuleset(libs.ktlint.compose.ruleset)
}
