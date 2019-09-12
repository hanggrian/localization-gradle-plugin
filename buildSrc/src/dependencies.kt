import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.plugin.use.PluginDependenciesSpec

fun DependencyHandler.ktor(module: String) = "io.ktor:ktor-$module:$VERSION_KTOR"

fun DependencyHandler.kotlinx(
    module: String,
    version: String? = null
) = "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" } ?: ""}"

fun DependencyHandler.kotlinPoet() = "com.squareup:kotlinpoet:$VERSION_KOTLINPOET"

fun DependencyHandler.truth() = "com.google.truth:truth:$VERSION_TRUTH"

fun DependencyHandler.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"
inline val PluginDependenciesSpec.dokka get() = id("org.jetbrains.dokka")

fun DependencyHandler.bintrayRelease() = "com.novoda:bintray-release:$VERSION_BINTRAY_RELEASE"
inline val PluginDependenciesSpec.`bintray-release` get() = id("com.novoda.bintray-release")

fun DependencyHandler.gitPublish() = "org.ajoberstar:gradle-git-publish:$VERSION_GIT_PUBLISH"
inline val PluginDependenciesSpec.`git-publish` get() = id("org.ajoberstar.git-publish")

fun DependencyHandler.guava() = "com.google.guava:guava:$VERSION_GUAVA-jre"

fun DependencyHandler.ktlint() = "com.pinterest:ktlint:$VERSION_KTLINT"
