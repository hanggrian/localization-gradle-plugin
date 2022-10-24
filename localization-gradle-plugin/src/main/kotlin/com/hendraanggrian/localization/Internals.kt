package com.hendraanggrian.localization

import com.google.common.collect.HashBasedTable
import org.gradle.api.provider.Property
import java.util.Locale

/**
 * Locale configurations are kept in a Guava row sorted table using the following format:
 * - Row = text key.
 * - Column = locale string (either "xx" or "xx-XX") to be converted to [Locale], see [forEachLocale].
 * - Value = localized string value.
 */
internal typealias LocaleTable = HashBasedTable<String, String, String>

/** Keep converted locales here for faster reuse. */
private val LOCALE_MAP = mutableMapOf<String, Locale>()

/**
 * Converting locale string to actual locale is necessary since [Locale] tends to convert old ISO
 * code.
 */
internal fun LocaleTable.forEachLocale(action: (column: String, locale: Locale) -> Unit): Unit =
    columnKeySet().forEach { column ->
        action(
            column,
            LOCALE_MAP.getOrPut(column) {
                when {
                    '-' !in column -> when (column) {
                        "en" -> Locale.ENGLISH
                        "fr" -> Locale.FRENCH
                        "de" -> Locale.GERMAN
                        "it" -> Locale.ITALIAN
                        "ja" -> Locale.JAPANESE
                        "ko" -> Locale.KOREAN
                        "zh" -> Locale.CHINESE
                        else -> Locale(column)
                    }

                    else -> {
                        val language = column.substringBefore('-')
                        val country = column.substringAfter('-')
                        when {
                            language == "zh" && country == "CN" -> Locale.CHINA
                            language == "zh" && country == "TW" -> Locale.TAIWAN
                            language == "fr" && country == "FR" -> Locale.FRANCE
                            language == "de" && country == "DE" -> Locale.GERMANY
                            language == "it" && country == "IT" -> Locale.ITALY
                            language == "ja" && country == "JP" -> Locale.JAPAN
                            language == "ko" && country == "KR" -> Locale.KOREA
                            language == "en" && country == "GB" -> Locale.UK
                            language == "en" && country == "US" -> Locale.US
                            language == "en" && country == "CA" -> Locale.CANADA
                            language == "fr" && country == "CA" -> Locale.CANADA_FRENCH
                            else -> Locale(language, country)
                        }
                    }
                }
            }
        )
    }

internal class LocalizationTextBuilder(private val table: Property<LocaleTable>) :
    LocalizationTextScope {
    lateinit var currentRow: String
    override fun add(language: String, value: String) {
        table.get().put(currentRow, language, value)
    }
}
