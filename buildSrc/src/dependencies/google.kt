const val VERSION_GUAVA = "29.0-jre"
const val VERSION_TRUTH = "1.0.1"

fun org.gradle.api.artifacts.dsl.DependencyHandler.google(module: String, version: String) =
    "com.google.$module:$module:$version"
