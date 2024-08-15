pluginManagement.repositories {
    gradlePluginPortal()
    mavenCentral()
}
dependencyResolutionManagement.repositories.mavenCentral()

rootProject.name = "localization-gradle-plugin"

include("localization-gradle-plugin")
include("codegen")
include("website")
includeDir("samples")

fun includeDir(dir: String) =
    include(
        *file(dir)
            .listFiles()!!
            .filter { it.isDirectory }
            .map { "$dir:${it.name}" }
            .toTypedArray(),
    )
