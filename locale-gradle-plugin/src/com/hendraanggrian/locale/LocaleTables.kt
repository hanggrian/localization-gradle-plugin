package com.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import java.util.Locale

/**
 * Locale configurations are kept in a Guava row sorted table,
 * below are data structure example of the table.
 *
 * ```
 * +----------+----------+---------+
 * |          |    en    |    id   |
 * +----------+----------+---------+
 * | about    | About    | Tentang |
 * +----------+----------+---------+
 * | home     | Home     | Beranda |
 * +----------+----------+---------+
 * | settings | Settings | Setelan |
 * +----------+----------+---------+
 * ```
 */
typealias LocaleTable = TreeBasedTable<String, Locale, String>

internal fun localeTableOf(): LocaleTable = LocaleTable.create(
    { o1, o2 -> o1.compareTo(o2) },
    { o1, o2 -> o1.language.compareTo(o2.language) })

/**
 * Superinterface of table builders.
 *
 * @see LocaleTableColumnBuilder
 * @see LocaleTableRowBuilder
 */
interface LocaleTableScope {

    /** Data source containing localization configuration. */
    val table: LocaleTable
}
