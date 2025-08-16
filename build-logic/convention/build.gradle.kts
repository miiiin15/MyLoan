import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.miiiin15.myloan.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradleApiPlugin)
//    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.spotless)
    compileOnly(libs.detekt)
    implementation(libs.truth)
    implementation(libs.gradle)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("appConvention") {
            id = libs.plugins.convention.app.get().pluginId
            implementationClass = "AppConventionPlugin"
        }
        register("libraryConvention") {
            id = libs.plugins.convention.library.get().pluginId
            implementationClass = "LibraryConventionPlugin"
        }
        register("kotlinConvention") {
            id = libs.plugins.convention.kotlin.get().pluginId
            implementationClass = "KotlinConventionPlugin"
        }
        register("testConvention") {
            id = libs.plugins.convention.test.get().pluginId
            implementationClass = "TestConventionPlugin"
        }
//        register("spotlessConvention") {
//            id = "convention.spotless"
//            implementationClass = "SpotlessConventionPlugin"
//        }
//        register("detektConvention") {
//            id = "convention.detekt"
//            implementationClass = "DetektConventionPlugin"
//        }
    }
}