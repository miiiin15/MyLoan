import org.gradle.api.Plugin
import org.gradle.api.Project

class AppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("local.kotlin")
            pluginManager.apply("local.spotless")
            pluginManager.apply("com.google.devtools.ksp")
        }
    }
}