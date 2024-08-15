package com.hanggrian.localization.internal

import com.hanggrian.localization.LocaleTable
import com.hanggrian.localization.LocalizationExtension
import com.hanggrian.localization.LocalizationTextBuilder
import com.hanggrian.localization.LocalizationTextScope
import com.opencsv.CSVReader
import org.gradle.api.Action
import org.gradle.api.logging.Logger
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.property
import java.io.File
import java.util.Locale

/**
 * Gradle extension to configure localization, any changes made here will take effect
 * in [com.hanggrian.localization.AbstractLocalizeTask].
 */
public open class DefaultLocalizationExtension(objects: ObjectFactory, private val logger: Logger) :
    LocalizationExtension {
    final override val table: Property<LocaleTable> =
        objects
            .property<LocaleTable>()
            .convention(LocaleTable.create())

    final override val resourceName: Property<String> =
        objects
            .property<String>()
            .convention("strings")

    final override val defaultLocale: Property<Locale> =
        objects
            .property<Locale>()
            .convention(Locale.ENGLISH)

    private val textBuilder = LocalizationTextBuilder(table)

    final override fun text(key: String, configuration: Action<in LocalizationTextScope>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }

    final override fun importCsv(file: File) {
        logger.debug("Importing '${file.name}'")

        val result = CSVReader(file.inputStream().bufferedReader()).readAll()
        val columns = result.first().drop(1)
        logger.debug("Columns = {}", columns)

        val rows = result.drop(1)
        logger.debug("Rows = ${rows.size}")
        result.drop(1).forEach { line ->
            val row = line.first()
            val cells = line.drop(1)
            text(row) { scope ->
                cells.forEachIndexed { index, value ->
                    val column = columns[index]
                    when {
                        '-' !in column -> scope.add(column, value)
                        else ->
                            scope.add(
                                column.substringBefore('-'),
                                column.substringAfter('-'),
                                value,
                            )
                    }
                }
            }
        }
    }
}
