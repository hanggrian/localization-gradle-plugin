package com.hendraanggrian.locale

import java.util.Locale

/**
 * Second/last phase of localization configuration, used only within [LocaleRowBuilder] scope.
 *
 * However, this interface often isn't directly used, as [LocaleColumnBuilder] offers convenient
 * method on each possible localization.
 */
interface BaseLocaleColumnBuilder : LocaleTableScope {

    /**
     * Register localization value to specified [locale].
     *
     * @param locale column in the table.
     * @param value cell in the table.
     */
    fun column(locale: Locale, value: String)

    /**
     * Register localization value to locale with specified [language] and [country].
     *
     * @param language 2-digit code that will be used as [Locale]'s language.
     * @param country 2-digit code that will be used as [Locale]'s country.
     * @param value cell in the table.
     */
    fun column(language: String, country: String?, value: String)
}
