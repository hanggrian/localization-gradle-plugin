const val VERSION_KTOR = "1.3.2"

fun org.gradle.api.artifacts.dsl.DependencyHandler.ktor(module: String) =
    "io.ktor:ktor-$module:$VERSION_KTOR"
