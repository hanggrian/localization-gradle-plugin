package com.hendraanggrian.locale

import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.util.Locale

/** Gradle extension to configure localization, any changes made here will take affect in [LocalizeTask]. */
open class LocaleExtension : LocaleTableBuilder {
    private val allColumn = LocaleTableValueBuilderImpl()
    private val javaRow = LocaleTableBuilderImpl()
    private val androidRow = LocaleTableBuilderImpl()

    var resourceName: String = "strings"
    var defaultLocale: Locale? = null

    override fun configure(key: String, action: Action<LocaleTableValueBuilder>) {
        allColumn.currentRow = key
        action(allColumn)
    }

    fun configureJavaOnly(action: Action<LocaleTableBuilder>): Unit =
        action(javaRow)

    fun javaOnly(action: LocaleTableBuilder.() -> Unit): Unit =
        configureJavaOnly(action)

    fun configureAndroidOnly(action: Action<LocaleTableBuilder>): Unit =
        action(androidRow)

    fun androidOnly(action: LocaleTableBuilder.() -> Unit): Unit =
        configureAndroidOnly(action)

    internal val javaTable: LocaleTable
        get() = LocaleTable.create(allColumn.table).apply { putAll(javaRow.table) }

    internal val androidTable: LocaleTable
        get() = LocaleTable.create(allColumn.table).apply { putAll(androidRow.table) }

    private class LocaleTableBuilderImpl : LocaleTableBuilder {
        private val column = LocaleTableValueBuilderImpl()

        override fun configure(key: String, action: Action<LocaleTableValueBuilder>) {
            column.currentRow = key
            action(column)
        }

        internal val table: LocaleTable
            get() = column.table
    }

    private class LocaleTableValueBuilderImpl : LocaleTableValueBuilder {
        private val localeMap: MutableMap<String, Locale> = mutableMapOf()
        val table: LocaleTable = LocaleTable.create(
            { o1, o2 -> o1.compareTo(o2) },
            { o1, o2 -> o1.language.compareTo(o2.language) })
        lateinit var currentRow: String

        override fun add(locale: Locale, value: String) {
            table.put(currentRow, locale, value)
        }

        override fun add(language: String, country: String?, value: String) {
            val isCountryAvailable = country?.isNotBlank() ?: false
            add(
                localeMap.getOrPut(
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
