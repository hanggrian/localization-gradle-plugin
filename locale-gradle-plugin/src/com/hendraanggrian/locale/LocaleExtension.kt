@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.locale

import org.gradle.api.Action
import org.gradle.api.logging.Logger
import org.gradle.kotlin.dsl.invoke
import java.io.File
import java.util.Locale

/** Gradle extension to configure localization, any changes made here will take affect in [LocalizeTask]. */
open class LocaleExtension(
    override val projectDir: File,
    override val logger2: Logger
) : LocaleConfiguration, LocaleTableBuilder {

    internal val table: LocaleTable = LocaleTable.create()
    private val textBuilder = LocaleTextBuilderImpl(table)

    override var resourceName: String = "strings"
    override var defaultLocale: Locale? = null
    override var isSorted: Boolean = false
    override lateinit var outputDir: File

    override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }
}
