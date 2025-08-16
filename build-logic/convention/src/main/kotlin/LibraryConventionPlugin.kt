import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.apply
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        apply(plugin = "com.android.library")
        apply(plugin = "org.jetbrains.kotlin.android")
        apply(plugin = "com.google.devtools.ksp")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        var compileSdk = libs.findVersion("compileSdk").get().toString().toInt()
        var minSdk = libs.findVersion("minSdk").get().toString().toInt()

        extensions.configure<LibraryExtension> {
            this.compileSdk = compileSdk
            namespace = "com.miiiin15.myloan"

            defaultConfig {
                this.minSdk = minSdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildFeatures {
                compose = true
                viewBinding = true
                buildConfig = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            testOptions {
                unitTests.isReturnDefaultValues = true
            }

            packaging {
                resources.excludes.addAll(
                    listOf(
                        "META-INF/AL2.0",
                        "META-INF/licenses/**",
                        "**/attach_hotspot_windows.dll",
                        "META-INF/LGPL2.1"
                    )
                )
            }

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_17.toString()
                }
            }
        }
    }
}