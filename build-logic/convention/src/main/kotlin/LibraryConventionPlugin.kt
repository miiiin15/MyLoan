import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.library")
        pluginManager.apply("local.kotlin")
        pluginManager.apply("local.test")
        pluginManager.apply("androidx.navigation.safeargs.kotlin")
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure(LibraryExtension::class.java) { ext ->
            val libs = extensions.getByType<LibrariesForLibs>()
            ext.namespace = "com.miiiin15.myloan"
            ext.compileSdk = libs.versions.compileSdk.get().toInt()
            ext.defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }
            // 이하 생략: composeOptions, compileOptions, kotlinOptions, testOptions, packaging 등
        }
    }
}