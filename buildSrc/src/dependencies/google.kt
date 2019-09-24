import org.gradle.api.artifacts.dsl.DependencyHandler

const val VERSION_GUAVA = "28.1-jre"
const val VERSION_TRUTH = "1.0"

fun DependencyHandler.google(module: String, version: String): String =
    "com.google.$module:$module:$version"