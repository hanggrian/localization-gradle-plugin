package com.hendraanggrian.locale

import org.gradle.api.Action

/**
 * Starting point of localization configuration.
 *
 * @see LocaleExtension
 * @see LocaleExtension.configureAndroidOnly
 * @see LocaleExtension.configureJavaOnly
 */
interface LocaleTableRowBuilder : LocaleTableScope {

    /**
     * Marks [key] as current row and opening closure to modify that row.
     *
     * @param key specified row.
     * @param action closure to populate localization table.
     */
    fun row(key: String, action: Action<LocaleTableColumnBuilder>)

    /** Alias of [row] for Gradle Kotlin DSL. */
    operator fun String.invoke(action: LocaleTableColumnBuilder.() -> Unit): Unit =
        row(this, action)
}
