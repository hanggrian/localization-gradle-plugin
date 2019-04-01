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

    var resourceName: String? = null
    var defaultLocale: Locale = Locale.ENGLISH

    operator fun String.invoke(action: Action<LocaleBuilder>) {
        impl.currentKey = this
        impl.also { action.execute(it) }
    }

    internal val table: RowSortedTable<String, Locale, String> get() = impl.table

    private class LocaleBuilderImpl : LocaleBuilder {
        val localeCache: MutableMap<String, Locale> = mutableMapOf()
        val table: RowSortedTable<String, Locale, String> = TreeBasedTable.create(
            Comparator<String> { o1, o2 -> o1.compareTo(o2) },
            Comparator<Locale> { o1, o2 -> o1.language.compareTo(o2.language) })
        lateinit var currentKey: String

        override fun add(locale: Locale, value: String) {
            table.put(currentKey, locale, value)
        }

        override fun add(language: String, country: String?, value: String) {
            val isCountryAvailable = country != null && country.isNotBlank()
            add(
                localeCache.getOrPut(
                    when {
                        isCountryAvailable -> "$language-$country"
                        else -> language
                    }
                ) {
                    when {
                        isCountryAvailable -> Locale(language, country)
                        else -> Locale(language)
                    }
                }, value
            )
        }
    }
}