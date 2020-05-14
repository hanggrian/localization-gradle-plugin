package com.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import java.util.Locale

/** Locale configurations are kept in a Guava row sorted table. */
internal typealias LocaleTable = TreeBasedTable<String, Locale, String>

internal fun localeTableOf(): LocaleTable = LocaleTable.create(
    { o1, o2 -> o1.compareTo(o2) },
    { o1, o2 -> o1.language.compareTo(o2.language) })

/**
 * Superinterface of table builders.
 *
 * @see LocaleTableBuilder
 * @see LocaleTextBuilder
 */
interface LocaleTableScope {

    /** Data source containing localization configuration. */
    val table: LocaleTable
}
