package com.hendraanggrian.localization

import org.gradle.api.provider.Property
import java.util.*

/** Extension instance when configuring `localization` in Gradle scripts. */
interface Localization : LocalizeSpec {
    /**
     * Default locale, when matching localization is found, file name suffix is removed.
     * Default is [Locale.ENGLISH].
     */
    val defaultLocale: Property<Locale>
}
