const val VERSION_KOTLIN = "1.3.61"
const val VERSION_COROUTINES = "1.3.3"
private const val VERSION_DOKKA: String = "0.10.0"

fun Dependencies.kotlinx(module: String, version: String? = null) =
    "org.jetbrains.kotlinx:kotlinx-$module${version?.let { ":$it" } ?: ""}"

fun Dependencies.dokka() = "org.jetbrains.dokka:dokka-gradle-plugin:$VERSION_DOKKA"

val Plugins.dokka get() = id("org.jetbrains.dokka")