package com.hendraanggrian.locale

import com.opencsv.CSVReader
import org.gradle.api.Action
import org.gradle.api.logging.Logger
import java.io.File

/**
 * Starting point of localization configuration.
 * @see LocaleExtension
 * @see AbstractLocalizeTask
 */
interface LocaleTableBuilder {

    /** Prints debugging messages of CSV import. Named accordingly to avoid name clash with [org.gradle.api.Task]. */
    fun getLogger(): Logger

    /**
     * Marks [key] as current row and opening closure to modify that row.
     * @param key specified row.
     * @param configuration closure to populate localization table.
     */
    fun text(key: String, configuration: Action<LocaleTextBuilder>)

    /** Alias of [text] for Gradle Kotlin DSL. */
    operator fun String.invoke(configuration: LocaleTextBuilder.() -> Unit): Unit =
        text(this, configuration)

    /**
     * Import CSV file and add it to existing table, not replacing them.
     * In this sense, it is possible to import multiple files.
     * The CSV file in question must have a header with format `key;locale1;...;localeN`
     */
    fun importCSV(file: File) {
        getLogger().debug("Importing '${file.name}'")

        val result = CSVReader(file.inputStream().bufferedReader()).readAll()
        val columns = result.first().drop(1)
        getLogger().debug("Columns = $columns")

        val rows = result.drop(1)
        getLogger().debug("Rows = ${rows.size}")
        result.drop(1).forEach { line ->
            val row = line.first()
            val cells = line.drop(1)
            text(row) {
                cells.forEachIndexed { index, value ->
                    val column = columns[index]
                    when {
                        '-' !in column -> add(column, value)
                        else -> add(column.substringBefore('-'), column.substringAfter('-'), value)
                    }
                }
            }
        }
    }
}
