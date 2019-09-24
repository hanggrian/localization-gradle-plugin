import org.gradle.api.artifacts.dsl.DependencyHandler

internal const val VERSION_KTLINT = "0.34.2"

fun DependencyHandler.ktlint(): String =
    "com.pinterest:ktlint:$VERSION_KTLINT"