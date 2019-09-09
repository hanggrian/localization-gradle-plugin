package com.hendraanggrian.locale

import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.util.Locale

/** Gradle extension to row localization, any changes made here will take affect in [LocalizeTask]. */
open class LocaleExtension : LocaleRowBuilder {
    private val allColumn = LocaleColumnBuilderImpl()
    private val javaRow = LocaleRowBuilderImpl()
    private val androidRow = LocaleRowBuilderImpl()

    var resourceName: String = "strings"
    var defaultLocale: Locale? = null

    override fun row(key: String, action: Action<LocaleColumnBuilder>) {
        allColumn.currentRow = key
        action(allColumn)
    }

    fun configureJavaOnly(action: Action<LocaleRowBuilder>): Unit =
        action(javaRow)

    fun javaOnly(action: LocaleRowBuilder.() -> Unit): Unit =
        configureJavaOnly(action)

    fun configureAndroidOnly(action: Action<LocaleRowBuilder>): Unit =
        action(androidRow)

    fun androidOnly(action: LocaleRowBuilder.() -> Unit): Unit =
        configureAndroidOnly(action)

    internal val javaTable: LocaleTable
        get() = LocaleTable.create(allColumn.table).apply { putAll(javaRow.table) }

    internal val androidTable: LocaleTable
        get() = LocaleTable.create(allColumn.table).apply { putAll(androidRow.table) }

    private class LocaleRowBuilderImpl : LocaleRowBuilder {
        private val column = LocaleColumnBuilderImpl()

        override fun row(key: String, action: Action<LocaleColumnBuilder>) {
            column.currentRow = key
            action(column)
        }

        internal val table: LocaleTable
            get() = column.table
    }

    private class LocaleColumnBuilderImpl : LocaleColumnBuilder {
        private companion object {
            private val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
        }

        val table: LocaleTable = LocaleTable.create(
            { o1, o2 -> o1.compareTo(o2) },
            { o1, o2 -> o1.language.compareTo(o2.language) })
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
