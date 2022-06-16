package com.hendraanggrian.localization

import java.util.*

/**
 * Second/last phase of localization configuration, used only within [LocalizeSpec] scope.
 *
 * However, this interface often isn't directly used,
 * as [LocalizationTextScope] offers convenient method on each possible localization.
 */
interface BaseLocalizationTextScope {

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
