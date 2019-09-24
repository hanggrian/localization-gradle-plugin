import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

internal const val VERSION_DOKKA: String = "0.9.18"

fun dokka(module: String? = null): String =
    "org.jetbrains.dokka:dokka${module?.let { "-$it" } ?: ""}-gradle-plugin:$VERSION_DOKKA"

fun PluginDependenciesSpec.dokka(module: String? = null): PluginDependencySpec =
    id("org.jetbrains.dokka${module?.let { "-$it" } ?: ""}")