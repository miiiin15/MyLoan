// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("local.detekt")
    id("local.spotless")
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
