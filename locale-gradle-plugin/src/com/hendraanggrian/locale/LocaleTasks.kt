package com.hendraanggrian.locale

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Locale
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Non-platform specific locale writer task. */
abstract class LocalizeTask : DefaultTask() {

    @Internal lateinit var table: LocaleTable

    /** Localization resource name, default is `strings`. */
    @Input var resourceName: String? = null

    /** Default locale, when matching localization is found, file name suffix is removed. */
    @Input @Optional var defaultLocale: Locale? = null

    /** Path that localization files will be generated to. */
    @OutputDirectory var outputDir: File? = null

    /** Convenient method to set output directory from file path, relative to project directory. */
    var outputDirectory: String
        @OutputDirectory get() = outputDir!!.absolutePath
        set(value) {
            outputDir = project.projectDir.resolve(value)
        }

    @TaskAction
    @Throws(IOException::class)
    fun generate() {
        require(!resourceName.isNullOrBlank()) { "Resource name cannot be blank" }
        require(outputDir != null && outputDir!!.isDirectory) { "Output directory is not valid" }

        logger.log(LogLevel.INFO, "Preparing localization")
        outputDir!!.mkdirs()
        logger.log(LogLevel.INFO, "Writing localization")
        write()
        logger.log(LogLevel.INFO, "Localization done")
    }

    /** Actual file writing process goes here. */
    abstract fun write()

    @Internal
    internal fun Locale.toSuffix(separator: Char): String = buildString {
        if (this@toSuffix == defaultLocale) {
            return@buildString
        }
        append("$separator$language")
        if (country.isNotBlank()) {
            append("$separator$country")
        }
    }

    @Internal
    internal fun File.deleteIfExists() {
        if (exists()) {
            delete()
        }
    }

    internal val fileComment: String
        @Internal get() = "Generated file by locale-gradle-plugin, do not edit manually."
}

/** Task to write properties files which will then be used as [java.util.ResourceBundle]. */
open class LocalizeJavaTask : LocalizeTask() {

    override fun write() = table.columnKeySet().forEach { locale ->
        val properties = Properties()
        table.rowKeySet().forEach { key ->
            properties[key] = table[key, locale]
        }
        val outputFile = outputDir!!.resolve("$resourceName${locale.toSuffix('_')}.properties")
        outputFile.deleteIfExists()
        outputFile.outputStream().use {
            properties.store(it, fileComment)
        }
    }
}

/** Task to write Android string resources files. */
open class LocalizeAndroidTask : LocalizeTask() {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory.newInstance().apply {
        setAttribute("indent-number", 4)
    }.newTransformer().apply {
        setOutputProperty(OutputKeys.INDENT, "yes")
        // http://stackoverflow.com/a/18251901/3375325
        setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes")
    }

    override fun write() = table.columnKeySet().forEach { locale ->
        val doc = docBuilder.newDocument().apply { xmlStandalone = true }
        doc.appendChild(doc.createComment(fileComment))
        val resources = doc.appendChild(doc.createElement("resources"))
        table.rowKeySet().forEach { key ->
            val s = doc.createElement("string")
            s.setAttribute("name", key)
            s.appendChild(doc.createTextNode(table[key, locale]))
            resources.appendChild(s)
        }
        val innerOutputDir = outputDir!!.resolve("values${locale.toSuffix('-')}")
        innerOutputDir.mkdirs()
        val outputFile = innerOutputDir.resolve("$resourceName.xml")
        outputFile.deleteIfExists()
        transformer.transform(DOMSource(doc), StreamResult(FileWriter(outputFile)))
    }
}
