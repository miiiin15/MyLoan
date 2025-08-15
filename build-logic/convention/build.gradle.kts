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
            id = "convention.app"
            implementationClass = "AppConventionPlugin"
        }
        register("libraryConvention") {
            id = "convention.library"
            implementationClass = "LibraryConventionPlugin"
        }
        register("kotlinConvention") {
            id = "convention.kotlin"
            implementationClass = "KotlinConventionPlugin"
        }
        register("testConvention") {
            id = "convention.test"
            implementationClass = "TestConventionPlugin"
        }
        register("spotlessConvention") {
            id = "convention.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
        register("detektConvention") {
            id = "convention.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}