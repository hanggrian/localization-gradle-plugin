import org.gradle.api.artifacts.dsl.DependencyHandler

internal const val VERSION_KTOR = "1.2.4"

fun DependencyHandler.ktor(module: String): String =
    "io.ktor:ktor-$module:$VERSION_KTOR"
