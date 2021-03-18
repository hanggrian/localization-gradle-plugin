package io.github.hendraanggrian.locale

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import java.util.Locale

internal interface LocaleConfiguration {

    /**
     * Localization resource name.
     * Default is `strings`.
     */
    val resourceName: Property<String>

    /**
     * Default locale, when matching localization is found, file name suffix is removed.
     * Default is `null`.
     */
    val defaultLocale: Property<Locale>

    /**
     * When enabled, generated localization will maintain its alphabetical order.
     * Default is `false`.
     */
    val sortValues: Property<Boolean>

    /**
     * The output directory.
     * Default is `src/main/resources`.
     */
    val outputDirectory: DirectoryProperty
}
