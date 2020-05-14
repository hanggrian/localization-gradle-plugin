package com.hendraanggrian.locale

import org.gradle.api.Action

/**
 * Starting point of localization configuration.
 *
 * @see LocaleExtension
 * @see LocaleExtension.configureAndroidOnly
 * @see LocaleExtension.configureJavaOnly
 */
interface LocaleTableBuilder : LocaleTableScope {

    /**
     * Marks [key] as current row and opening closure to modify that row.
     *
     * @param key specified row.
     * @param configuration closure to populate localization table.
     */
    fun text(key: String, configuration: Action<LocaleTextBuilder>)

    /** Alias of [text] for Gradle Kotlin DSL. */
    operator fun String.invoke(configuration: LocaleTextBuilder.() -> Unit): Unit =
        text(this, configuration)
}
