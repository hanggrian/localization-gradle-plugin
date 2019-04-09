package com.hendraanggrian.locale

import java.util.Locale

/** Base interface used to configure localization content. */
interface BaseLocaleBuilder {

    /** Register localization value to specified [locale]. */
    fun add(locale: Locale, value: String)

    /** Register localization value to locale with specified [language] and [country]. */
    fun add(language: String, country: String?, value: String)
}