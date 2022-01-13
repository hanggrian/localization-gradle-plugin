internal typealias Dependencies = org.gradle.api.artifacts.dsl.DependencyHandler
internal typealias Plugins = org.gradle.plugin.use.PluginDependenciesSpec

val Dependencies.`gradle-publish` get() = "com.gradle.publish:plugin-publish-plugin:0.18.0"
val Plugins.`gradle-publish` get() = id("com.gradle.plugin-publish")

const val VERSION_KOTLIN = "1.6.10"
const val VERSION_DOKKA = "1.6.0"
const val VERSION_COROUTINES = "1.6.0"
val Dependencies.dokka get() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"
val Plugins.dokka get() = id("org.jetbrains.dokka")
fun Dependencies.kotlinx(module: String, version: String? = null) =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" }.orEmpty()}"

val Dependencies.`git-publish` get() = "org.ajoberstar:gradle-git-publish:3.0.0"
val Plugins.`git-publish` get() = id("org.ajoberstar.git-publish")

const val VERSION_GUAVA = "31.0.1-jre"
const val VERSION_TRUTH = "1.0.1"
fun Dependencies.google(module: String, version: String) = "com.google.$module:$module:$version"

const val VERSION_KOTLINPOETKTX = "0.1-SNAPSHOT"
fun Dependencies.hendraanggrian(module: String, version: String) = "com.hendraanggrian:$module:$version"
fun Dependencies.hendraanggrian(repo: String, module: String, version: String) =
    "com.hendraanggrian.$repo:$module:$version"

fun Dependencies.ktor(module: String) = "io.ktor:ktor-$module:1.6.7"

fun Dependencies.opencsv() = "com.opencsv:opencsv:5.5.2"