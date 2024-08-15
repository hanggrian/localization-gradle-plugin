package com.hanggrian.localization

import org.gradle.api.provider.Property

/** A specification for generating localization resources. */
@LocalizationConfigurationDsl
public interface LocalizeSpec {
    /** Locale configurations to be written as resources. */
    public val table: Property<LocaleTable>

    /** Generated resources file or root folder name. Default is `strings`. */
    public val resourceName: Property<String>
}
