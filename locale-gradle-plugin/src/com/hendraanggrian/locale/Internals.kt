package com.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import java.util.Locale

/**
 * Locale values configured within [LocaleBuilder] is kept in a Guava table,
 * below are structure example of the table:
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
internal typealias LocaleTable = TreeBasedTable<String, Locale, String>