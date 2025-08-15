import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.diffplug.spotless")
        extensions.configure(SpotlessExtension::class.java) {
            kotlin {
                target("**/*.kt", "**/*.kts")
                targetExclude("**/buildSrc/build/**/*.*")
                ktlint()
                indentWithSpaces()
                endWithNewline()
            }
            isEnforceCheck = false
        }
    }
}
