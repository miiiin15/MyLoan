import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.gradle.jvm-test-suite")
        pluginManager.apply("com.adarshr.test-logger")
        tasks.withType(Test::class.java).configureEach {
            useJUnitPlatform()
            systemProperties = mapOf(
                "junit.jupiter.execution.parallel.enabled" to "true",
                "junit.jupiter.execution.parallel.mode.default " to "concurrent",
            )
        }
    }
}