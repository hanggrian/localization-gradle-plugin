package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Element
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class WriteLocaleTask : DefaultTask() {

    @Internal protected lateinit var table: RowSortedTable<String, Locale, String>

    /** Localization resource name. */
    @Input lateinit var resourceName: String

    /** Default locale, when matching localization is found, file name suffix is removed. */
    @Input lateinit var defaultLocale: Locale

    /** Path that localization files will be generated to. */
    @OutputDirectory lateinit var outputDir: File

    /** Convenient method to set output directory from file path, relative to project directory. */
    var outputDirectory: String
        @OutputDirectory get() = outputDir.absolutePath
        set(value) {
            outputDir = project.projectDir.resolve(value)
        }

    @TaskAction
    @Throws(IOException::class)
    fun generate() {
        logger.log(LogLevel.INFO, "Preparing localization")
        outputDir.mkdirs()

        logger.log(LogLevel.INFO, "Writing localization")
        write()
    }

    abstract fun write()

    internal fun setTable(table: RowSortedTable<String, Locale, String>) {
        this.table = table
    }

    internal fun Locale.toSuffix(separator: Char): String = buildString {
        if (this@toSuffix == defaultLocale) {
            return@buildString
        }
        append("$separator$language")
        if (country.isNotBlank()) {
            append("$separator$country")
        }
    }

    internal fun File.deleteIfExists() {
        if (exists()) {
            delete()
        }
    }
}

open class WriteResourceBundlesTask : WriteLocaleTask() {

    override fun write() = table.columnKeySet().forEach { locale ->
        val properties = Properties()
        table.rowKeySet().forEach { key ->
            properties[key] = table[key, locale]
        }
        val outputFile = outputDir.resolve("$resourceName${locale.toSuffix('_')}.properties")
        outputFile.deleteIfExists()
        outputFile.outputStream().use {
            properties.store(it, null)
        }
    }
}

open class WriteAndroidResourcesTask : WriteLocaleTask() {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory.newInstance().newTransformer()

    override fun write() = table.columnKeySet().forEach { locale ->
        val doc = docBuilder.newDocument()
        val resources = doc.createElement("resources") as Element
        resources.setAttribute("xmlns:android", "http://schemas.android.com/apk/res/android")
        doc.appendChild(resources)
        table.rowKeySet().forEach { key ->
            val s = doc.createElement("string")
            s.appendChild(doc.createTextNode(table[key, locale]))
            resources.appendChild(s)
        }
        val outputFile = outputDir.resolve("values${locale.toSuffix('-')}").resolve("strings.xml")
        outputFile.deleteIfExists()
        transformer.transform(DOMSource(doc), StreamResult(outputFile))
    }
}