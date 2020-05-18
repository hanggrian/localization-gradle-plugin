package com.hendraanggrian.locale

import com.opencsv.CSVReader
import java.io.File
import org.gradle.api.Action

/**
 * Starting point of localization configuration.
 *
 * @see LocaleExtension
 * @see LocalizeTask
 */
interface LocaleTableBuilder {

    /** Working directory of a [org.gradle.api.Project]. */
    val projectDir: File

    /**
     * Marks [key] as current row and opening closure to modify that row.
     *
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
        val result = CSVReader(file.inputStream().bufferedReader()).readAll()
        val columns = result.first().drop(1)
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

    /** Convenient method to import CSV from file path, relative to project directory. */
    fun importCSV(path: String): Unit = importCSV(projectDir.resolve(path))
}
