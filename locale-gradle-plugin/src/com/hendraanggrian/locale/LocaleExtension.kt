package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import com.google.common.collect.TreeBasedTable
import org.gradle.api.Action
import java.util.Locale

/**
 * +----------+----------+---------+
 * |          |    en    |    id   |
 * +----------+----------+---------+
 * | about    | About    | Tentang |
 * +----------+----------+---------+
 * | home     | Home     | Beranda |
 * +----------+----------+---------+
 * | settings | Settings | Setelan |
 * +----------+----------+---------+
 */
open class LocaleExtension {
    private val impl = LocaleBuilderImpl()

    var localeName: String? = null
    var defaultLocale: Locale = Locale.ENGLISH

    operator fun String.invoke(action: Action<LocaleBuilder>) {
        impl.currentKey = this
        impl.also { action.execute(it) }
    }

    internal val table: RowSortedTable<String, Locale, String> get() = impl.table

    private class LocaleBuilderImpl : LocaleBuilder {
        val table: RowSortedTable<String, Locale, String> = TreeBasedTable.create(
            Comparator<String> { o1, o2 -> o1.compareTo(o2) },
            Comparator<Locale> { o1, o2 -> o1.language.compareTo(o2.language) })
        lateinit var currentKey: String

        override fun Locale.invoke(value: String) {
            table.put(currentKey, this, value)
        }
    }
}