const val VERSION_KTOR = "1.5.3"

fun org.gradle.api.artifacts.dsl.DependencyHandler.ktor(module: String) =
    "io.ktor:ktor-$module:$VERSION_KTOR"
