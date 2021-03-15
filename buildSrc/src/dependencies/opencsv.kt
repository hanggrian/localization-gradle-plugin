const val VERSION_OPENCSV = "5.1"

fun org.gradle.api.artifacts.dsl.DependencyHandler.opencsv() =
    "com.opencsv:opencsv:$VERSION_OPENCSV"