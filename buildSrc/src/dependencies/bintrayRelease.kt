import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

internal const val VERSION_BINTRAY_RELEASE = "0.9.1"

fun DependencyHandler.bintrayRelease(): String =
    "com.novoda:bintray-release:$VERSION_BINTRAY_RELEASE"

inline val PluginDependenciesSpec.`bintray-release`: PluginDependencySpec
    get() = id("com.novoda.bintray-release")