import org.gradle.api.artifacts.dsl.DependencyHandler

const val VERSION_KOTLINPOET_KTX = "0.1"

fun DependencyHandler.hendraanggrian(module: String, version: String): String =
    "com.hendraanggrian:$module:$version"