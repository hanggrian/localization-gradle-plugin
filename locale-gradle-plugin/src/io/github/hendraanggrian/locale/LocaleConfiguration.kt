package io.github.hendraanggrian.locale

import java.io.File
import java.util.Locale

internal interface LocaleConfiguration {

    /** Working directory of a [org.gradle.api.Project]. */
    val projectDir: File

    /**
     * Localization resource name.
     * Default is `strings`.
     */
    var resourceName: String

    /**
     * Default locale, when matching localization is found, file name suffix is removed.
     * Default is `null`.
     */
    var defaultLocale: Locale?

    /**
     * When enabled, generated localization will maintain its alphabetical order.
     * Default is `false`.
     */
    var isSorted: Boolean

    /**
     * The output directory.
     * Default is `src/main/resources`.
     */
    var outputDir: File

    /** Convenient method to set output directory from file path, relative to project directory. */
    var outputDirectory: String
        get() = outputDir.absolutePath
        set(value) {
            outputDir = projectDir.resolve(value)
        }
}
