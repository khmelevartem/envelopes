import org.gradle.kotlin.dsl.ktlintRuleset

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.room) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

dependencies {
    ktlintRuleset(libs.ktlint.compose.ruleset)
}
