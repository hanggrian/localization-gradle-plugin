include("localization-plugin")
include("codegen")
include("website")
includeDir("example")

fun includeDir(dir: String) = file(dir)
    .listFiles()!!
    .filter { it.isDirectory }
    .forEach { include("$dir:${it.name}") }