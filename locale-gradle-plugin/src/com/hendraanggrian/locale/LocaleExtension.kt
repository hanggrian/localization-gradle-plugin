@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.locale

import java.io.File
import java.util.Locale
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke

/**
 * Gradle extension to configure localization,
 * any changes made here will take affect in [LocalizeTask].
 */
open class LocaleExtension(override val projectDir: File) : LocaleConfiguration, LocaleTableBuilder {
    private val textBuilder = LocaleTextBuilderImpl()

    internal val table: LocaleTable get() = textBuilder.table

    override var resourceName: String = "strings"
    override var defaultLocale: Locale? = null
    override var isSorted: Boolean = true
    override lateinit var outputDir: File

    override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }
}
