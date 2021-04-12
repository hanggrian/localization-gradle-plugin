package com.hendraanggrian.lokkal

import java.util.Locale

/**
 * Second/last phase of localization configuration, used only within [LokkalTableBuilder] scope.
 *
 * However, this interface often isn't directly used,
 * as [LokkalTextBuilder] offers convenient method on each possible localization.
 */
interface BaseLokkalTextBuilder {

    /**
     * Register localization value to locale with specified [language].
     * @param language 2-digit code that will be used as [Locale]'s language.
     * @param value cell in the table.
     */
    fun add(language: String, value: String)

    /**
     * Register localization value to locale with specified [language] and [country].
     * @param language 2-digit code that will be used as [Locale]'s language.
     * @param country 2-digit code that will be used as [Locale]'s country.
     * @param value cell in the table.
     */
    fun add(language: String, country: String, value: String): Unit =
        add("$language-$country", value)
}
