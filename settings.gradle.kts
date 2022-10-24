include("localization-gradle-plugin")
include("codegen")
include("website")
includeDir("samples")

fun includeDir(dir: String) = file(dir)
    .listFiles()!!
    .filter { it.isDirectory }
    .forEach { include("$dir:${it.name}") }
