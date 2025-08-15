// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("local.detekt")
    id("local.spotless")
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    //    alias(libs.plugins.android.application) apply false
    //    alias(libs.plugins.compose.compiler) apply false
    //    alias(libs.plugins.kotlin.gradlePlugin) apply false
    //    alias(libs.plugins.ksp) apply false
    //    alias(libs.plugins.room) apply false
    //    alias(libs.plugins.testLogger) apply false
}
