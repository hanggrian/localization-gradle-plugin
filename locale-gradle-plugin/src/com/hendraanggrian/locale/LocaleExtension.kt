package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import com.google.common.collect.TreeBasedTable
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.util.Locale

/** Gradle extension to configure localization, any changes made here will take affect in [LocalizeTask]. */
open class LocaleExtension {
    private val impl = LocaleBuilderImpl()

    var resourceName: String = "strings"
    var defaultLocale: Locale = Locale.ENGLISH

    /** Opening closure to modify content of [key]. */
    fun configure(key: String, action: Action<LocaleBuilder>) {
        impl.currentRow = key
        impl.also { action(it) }
    }

    /** Alias of [configure] for Gradle Kotlin DSL. */
    operator fun String.invoke(action: LocaleBuilder.() -> Unit) = configure(this, action)

    internal val table: RowSortedTable<String, Locale, String> get() = impl.table

    /**
     * Locale values configured within [LocalBuilder] is kept in a Guava table, e.g.:
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
    private class LocaleBuilderImpl : LocaleBuilder {
        private val localeCache: MutableMap<String, Locale> = mutableMapOf()
        val table: RowSortedTable<String, Locale, String> = TreeBasedTable.create(
            { o1, o2 -> o1.compareTo(o2) },
            { o1, o2 -> o1.language.compareTo(o2.language) })
        lateinit var currentRow: String

        override fun add(locale: Locale, value: String) {
            table.put(currentRow, locale, value)
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