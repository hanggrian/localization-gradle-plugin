package com.hanggrian.localization

import java.util.Collections
import java.util.Enumeration
import java.util.Locale
import java.util.Properties
import java.util.TreeSet

/**
 * Task to run when `localizeJvm` command is executed. It writes properties files which will then be
 * used as [java.util.ResourceBundle].
 */
public open class LocalizeJvmTask : AbstractLocalizeTask() {
    final override fun onGenerateLocale(column: String, locale: Locale) {
        val properties = SortedProperties()
        table.get().rowKeySet().forEach { row -> properties[row] = table.get()[row, column] }

        outputDirectory.get().mkdirs()
        val outputFile =
            outputDirectory
                .get()
                .resolve("${resourceName.get()}${getSuffix(locale, '_')}.properties")
        outputFile.write { properties.store(it, getFileComment(false)) }
    }

    private class SortedProperties : Properties() {
        override fun keys(): Enumeration<Any> = Collections.enumeration(TreeSet(super.keys))
    }
}
