package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import com.google.common.collect.TreeBasedTable
import org.gradle.api.Action

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

    operator fun String.invoke(action: Action<LocaleBuilder>) {
        impl.currentKey = this
        impl.also { action.execute(it) }
    }

    internal val table: RowSortedTable<String, String, String> get() = impl.table

    private class LocaleBuilderImpl : LocaleBuilder {
        val table: RowSortedTable<String, String, String> = TreeBasedTable.create()
        lateinit var currentKey: String

        override fun en(value: String) {
            table.put(currentKey, "en", value)
        }

        override fun de(value: String) {
            table.put(currentKey, "de", value)
        }
    }
}