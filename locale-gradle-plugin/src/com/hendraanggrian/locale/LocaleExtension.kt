package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import com.google.common.collect.TreeBasedTable
import org.gradle.api.Action
import java.util.Locale

/** Gradle extension to configure localization, any changes made here will take affect in [WriteLocaleTask]. */
open class LocaleExtension {
    private val impl = LocaleBuilderImpl()

    var resourceName: String? = null
    var defaultLocale: Locale = Locale.ENGLISH

    /** Opening closure to modify content of [key]. */
    fun configure(key: String, action: Action<LocaleBuilder>) {
        impl.currentKey = key
        impl.also { action.execute(it) }
    }

    /** Alias of [configure] for Gradle Kotlin DSL. */
    operator fun String.invoke(action: LocaleBuilder.() -> Unit) = configure(this, action)

    internal val table: RowSortedTable<String, Locale, String> get() = impl.table

    /**
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