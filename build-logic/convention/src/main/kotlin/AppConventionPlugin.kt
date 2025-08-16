import org.gradle.api.Plugin
import org.gradle.api.Project

class AppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            pluginManager.apply("com.android.application")
            pluginManager.apply("convention.kotlin")
        }
    }
}