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

interface LocaleTableScope {

    /** Data source containing localization configuration. */
    val table: LocaleTable
}