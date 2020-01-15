include("locale-gradle-plugin")
include("locale-generator")
include("website")
includeDir("integration-tests")

fun includeDir(dir: String) = file(dir)
    .listFiles()
    .filter { it.isDirectory }
    .forEach { include("$dir:${it.name}") }