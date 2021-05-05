package com.hendraanggrian.localization

import org.gradle.api.provider.Property
import java.util.Locale

interface LocalizationExtension : LocalizeSpec {

    /**
     * Default locale, when matching localization is found, file name suffix is removed.
     * Default is `null`.
     */
    val defaultLocale: Property<Locale>
}
