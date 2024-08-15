package com.hanggrian.localization

import org.gradle.api.Action
import org.gradle.api.provider.Property
import java.io.File
import java.util.Locale

/** Extension instance when configuring `localization` in Gradle scripts. */
public interface LocalizationExtension : LocalizeSpec {
    /**
     * Default locale, when matching localization is found, file name suffix is removed. Default
     * is [Locale.ENGLISH].
     */
    public val defaultLocale: Property<Locale>

    /**
     * Marks [key] as current row and opening closure to modify that row.
     *
     * @param key specified row.
     * @param configuration closure to populate localization table.
     */
    public fun text(key: String, configuration: Action<in LocalizationTextScope>)

    /** Alias of [text] for Kotlin DSL. */
    public operator fun String.invoke(configuration: LocalizationTextScope.() -> Unit): Unit =
        text(this, configuration)

    /**
     * Import CSV file and add it to existing table, not replacing them. In this sense, it is
     * possible to import multiple files. The CSV file in question must have a header with
     * format `key;locale1;...;localeN`
     */
    public fun importCsv(file: File)
}
