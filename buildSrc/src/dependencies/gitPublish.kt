import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

internal const val VERSION_GIT_PUBLISH = "0.3.3"

fun DependencyHandler.gitPublish(): String =
    "org.ajoberstar:gradle-git-publish:$VERSION_GIT_PUBLISH"

inline val PluginDependenciesSpec.`git-publish`: PluginDependencySpec
    get() = id("org.ajoberstar.git-publish")