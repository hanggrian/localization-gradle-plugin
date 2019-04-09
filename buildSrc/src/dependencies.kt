import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec

fun DependencyHandler.ktor(module: String) = "io.ktor:ktor-$module:1.1.3"

fun DependencyHandler.kotlinx(
    module: String,
    version: String? = null
) = "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" } ?: ""}"

fun DependencyHandler.kotlinPoet() = "com.squareup:kotlinpoet:1.2.0"

fun DependencyHandler.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:0.9.17"
inline val PluginDependenciesSpec.dokka get() = id("org.jetbrains.dokka")

fun DependencyHandler.bintray() = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4"
inline val PluginDependenciesSpec.bintray get() = id("com.jfrog.bintray")

fun DependencyHandler.bintrayRelease() = "com.novoda:bintray-release:0.9"
inline val PluginDependenciesSpec.`bintray-release` get() = id("com.novoda.bintray-release")

fun DependencyHandler.gitPublish() = "org.ajoberstar:gradle-git-publish:0.3.3"
inline val PluginDependenciesSpec.`git-publish` get() = id("org.ajoberstar.git-publish")

fun DependencyHandler.guava() = "com.google.guava:guava:27.1-jre"

fun DependencyHandler.ktlint() = "com.github.shyiko:ktlint:0.31.0"