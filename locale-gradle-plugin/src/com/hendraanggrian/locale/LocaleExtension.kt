package com.hendraanggrian.locale

import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.util.Locale

/**
 * Gradle extension to configure localization,
 * any changes made here will take affect in [LocalizeTask].
 */
open class LocaleExtension : LocaleRowBuilder by LocaleRowBuilderImpl() {
    private val javaRowImpl = LocaleRowBuilderImpl()
    private val androidRowImpl = LocaleRowBuilderImpl()

    var resourceName: String = "strings"
    var defaultLocale: Locale? = null

    fun configureJavaOnly(action: Action<LocaleRowBuilder>): Unit =
        action(javaRowImpl)

    fun javaOnly(action: LocaleRowBuilder.() -> Unit): Unit =
        configureJavaOnly(action)

    fun configureAndroidOnly(action: Action<LocaleRowBuilder>): Unit =
        action(androidRowImpl)

    fun androidOnly(action: LocaleRowBuilder.() -> Unit): Unit =
        configureAndroidOnly(action)

    internal val javaTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(javaRowImpl.table) }

    internal val androidTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(androidRowImpl.table) }

    private class LocaleRowBuilderImpl : LocaleRowBuilder {
        private val columnImpl = LocaleColumnBuilderImpl()

        override val table: LocaleTable
            get() = columnImpl.table

        override fun row(key: String, action: Action<LocaleColumnBuilder>) {
            columnImpl.currentRow = key
            action(columnImpl)
        }
    }

    private class LocaleColumnBuilderImpl : LocaleColumnBuilder {
        private companion object {
            private val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
        }

        override val table: LocaleTable = LocaleTable.create(
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
