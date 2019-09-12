package com.hendraanggrian.locale

import java.util.Locale
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke

/**
 * Gradle extension to configure localization,
 * any changes made here will take affect in [LocalizeTask].
 */
open class LocaleExtension : LocaleTableRowBuilder by LocaleRowBuilderImpl() {
    private val javaRow = LocaleRowBuilderImpl()
    private val androidRow = LocaleRowBuilderImpl()

    /** Intended localization file name prefix, must not be empty. */
    var resourceName: String = "strings"

    /** Optional default localization. When set, will remove related 2-digit prefix of file name. */
    var defaultLocale: Locale? = null

    /** Opening closure to populate Java-only localization table. */
    fun configureJavaOnly(action: Action<LocaleTableRowBuilder>): Unit =
        action(javaRow)

    /** Alias of [configureJavaOnly] for Gradle Kotlin DSL. */
    fun javaOnly(action: LocaleTableRowBuilder.() -> Unit): Unit =
        configureJavaOnly(action)

    /** Opening closure to populate Android-only localization table. */
    fun configureAndroidOnly(action: Action<LocaleTableRowBuilder>): Unit =
        action(androidRow)

    /** Alias of [configureAndroidOnly] for Gradle Kotlin DSL. */
    fun androidOnly(action: LocaleTableRowBuilder.() -> Unit): Unit =
        configureAndroidOnly(action)

    internal val javaTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(javaRow.table) }

    internal val androidTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(androidRow.table) }

    private class LocaleRowBuilderImpl : LocaleTableRowBuilder {
        private val column = LocaleColumnBuilderImpl()

        override val table: LocaleTable
            get() = column.table

        override fun row(key: String, action: Action<LocaleTableColumnBuilder>) {
            column.currentRow = key
            action(column)
        }
    }

    private class LocaleColumnBuilderImpl : LocaleTableColumnBuilder {
        companion object {
            private val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
        }

        override val table: LocaleTable = localeTableOf()
        lateinit var currentRow: String

        override fun column(locale: Locale, value: String) {
            table.put(currentRow, locale, value)
        }

        override fun column(language: String, country: String?, value: String) {
            val isCountryAvailable = country?.isNotBlank() ?: false
            column(
                LOCALE_MAP.getOrPut(
                    when {
                        isCountryAvailable -> "$language-$country"
                        else -> language
                    }
                ) {
                    when {
                        isCountryAvailable -> Locale(language, country)
                        else -> Locale(language)
                    }
                }, value
            )
        }
    }
}
