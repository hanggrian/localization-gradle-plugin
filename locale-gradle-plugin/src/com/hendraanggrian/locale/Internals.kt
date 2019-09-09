package com.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import java.util.Locale

/**
 * Locale configurations are kept in a Guava row sorted table,
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

internal const val NO_GETTER: String = "Property does not have a getter."

internal fun noGetter(): Nothing = throw UnsupportedOperationException(NO_GETTER)