package com.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import java.util.Locale

/** Locale configurations are kept in a Guava row sorted table. */
internal typealias LocaleTable = TreeBasedTable<String, Locale, String>

internal fun localeTableOf(): LocaleTable = LocaleTable.create(
    { o1, o2 -> o1.compareTo(o2) },
    { o1, o2 -> o1.language.compareTo(o2.language) })

internal class LocaleTextBuilderImpl : LocaleTextBuilder {
    companion object {
        private val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
    }

    val table: LocaleTable = localeTableOf()
    lateinit var currentRow: String

    override fun add(locale: Locale, value: String) {
        table.put(currentRow, locale, value)
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
