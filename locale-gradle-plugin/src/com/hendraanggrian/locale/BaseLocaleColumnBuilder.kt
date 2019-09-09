package com.hendraanggrian.locale

import java.util.Locale

/** Base interface used to row localization content. */
interface BaseLocaleColumnBuilder {

    /**
     * Register localization value to specified [locale].
     *
     * @param locale column in the table.
     * @param value value in the table.
     */
    fun column(locale: Locale, value: String)

    /**
     * Register localization value to locale with specified [language] and [country].
     *
     * @param language 2-digit code that will be used as [Locale]'s language.
     * @param country 2-digit code that will be used as [Locale]'s country.
     * @param value value in the table.
     */
    fun column(language: String, country: String?, value: String)
}
