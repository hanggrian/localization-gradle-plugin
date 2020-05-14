@file:Suppress("NOTHING_TO_INLINE")

package com.hendraanggrian.locale

import java.util.Locale
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke

/**
 * Gradle extension to configure localization,
 * any changes made here will take affect in [LocalizeTask].
 */
open class LocaleExtension : LocaleTableBuilder by LocaleTableBuilderImpl() {
    private val _javaTable = LocaleTableBuilderImpl()
    private val _androidTable = LocaleTableBuilderImpl()

    /** Intended localization file name prefix, must not be empty. */
    var resourceName: String = "strings"

    /** Optional default localization. When set, will remove related 2-digit prefix of file name. */
    var defaultLocale: Locale? = null

    /** Opening closure to populate Java-only localization table. */
    fun configureJavaOnly(configuration: Action<LocaleTableBuilder>): Unit =
        configuration(_javaTable)

    /** Alias of [configureJavaOnly] for Gradle Kotlin DSL. */
    inline fun javaOnly(noinline configuration: LocaleTableBuilder.() -> Unit): Unit =
        configureJavaOnly(configuration)

    /** Opening closure to populate Android-only localization table. */
    fun configureAndroidOnly(configuration: Action<LocaleTableBuilder>): Unit =
        configuration(_androidTable)

    /** Alias of [configureAndroidOnly] for Gradle Kotlin DSL. */
    inline fun androidOnly(noinline configuration: LocaleTableBuilder.() -> Unit): Unit =
        configureAndroidOnly(configuration)

    internal val javaTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(_javaTable.table) }

    internal val androidTable: LocaleTable
        get() = LocaleTable.create(table).apply { putAll(_androidTable.table) }

    private class LocaleTableBuilderImpl : LocaleTableBuilder, LocaleTextBuilder {
        companion object {
            val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
        }

        override val table: LocaleTable = localeTableOf()
        lateinit var currentKey: String

        override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
            currentKey = key
            configuration(this)
        }

        override fun add(locale: Locale, value: String) {
            table.put(currentKey, locale, value)
        }

        override fun add(language: String, country: String?, value: String) {
            val isCountryAvailable = country?.isNotBlank() ?: false
            add(
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
