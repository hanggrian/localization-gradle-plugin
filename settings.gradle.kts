include(RELEASE_ARTIFACT)
include("locale-builder-generator")
include("website")
includeDir("demo")

fun includeDir(dir: String) = file(dir)
    .listFiles()
    .filter { it.isDirectory }
    .forEach { include("$dir:${it.name}") }