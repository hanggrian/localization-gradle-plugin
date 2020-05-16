package com.hendraanggrian.locale

import com.google.common.collect.Ordering
import com.google.common.collect.TreeBasedTable
import java.util.Locale

/** Locale configurations are kept in a Guava row sorted table. */
internal typealias LocaleTable = TreeBasedTable<String, Locale, String>

internal fun localeTableOf(): LocaleTable = LocaleTable.create(
    Ordering.natural(),
    Ordering.from { o1, o2 -> o1.language.compareTo(o2.language) })

internal class LocaleTextBuilderImpl(private val table: LocaleTable) : LocaleTextBuilder {
    private companion object {
        val LOCALE_MAP: MutableMap<String, Locale> = mutableMapOf()
    }

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
