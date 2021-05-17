package com.hendraanggrian.localization

import com.google.common.collect.Ordering
import com.hendraanggrian.localization.internal.AbstractLocalizeTask
import java.util.Collections
import java.util.Enumeration
import java.util.Locale
import java.util.Properties
import java.util.TreeSet

/** Task to write properties files which will then be used as [java.util.ResourceBundle]. */
open class LocalizeJvmTask : AbstractLocalizeTask() {

    final override fun onGenerateLocale(column: String, locale: Locale) {
        val properties = SortedProperties()
        table.get().rowKeySet().forEach { row -> properties[row] = table.get()[row, column] }

        outputDirectory.get().asFile.mkdirs()
        val outputFile = outputDirectory
            .dir("${resourceName.get()}${getSuffix(locale, '_')}.properties")
            .get().asFile
        outputFile.write { properties.store(it, getFileComment(false)) }
    }

    /**
     * Sorted properties that reportedly only works on Java 8.
     * See [StackOverflow](https://stackoverflow.com/a/52127284/1567541).
     */
    private class SortedProperties : Properties() {
        companion object {
            const val serialVersionUID = 1L
        }

        override val keys: MutableSet<Any> get() = Collections.unmodifiableSet(TreeSet(super.keys))

        override val entries: MutableSet<MutableMap.MutableEntry<Any, Any>>
            get() {
                val set1 = super.entries
                val set2 = LinkedHashSet<MutableMap.MutableEntry<Any, Any>>(set1.size)
                set1.sortedWith(Ordering.from { o1, o2 -> "${o1.key}".compareTo("${o2.key}") })
                    .forEach { set2.add(it) }
                return set2
            }

        override fun keys(): Enumeration<Any> = Collections.enumeration(TreeSet(super.keys))
    }
}
