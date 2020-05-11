package com.hendraanggrian.locale

import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.Enumeration
import java.util.Locale
import java.util.Properties
import java.util.TreeSet
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/** Non-platform specific locale writer task. */
sealed class LocalizeTask : DefaultTask() {

    @Internal protected lateinit var table: LocaleTable

    /** Localization resource name, default is `strings`. */
    @Input var resourceName: String? = null

    /** Default locale, when matching localization is found, file name suffix is removed. */
    @Input @Optional var defaultLocale: Locale? = null

    /** When enabled, generated localization will maintain its alphabetical order. */
    @Input var sortRows: Boolean = true

    /** Path that localization files will be generated to. */
    @OutputDirectory var outputDir: File? = null

    /** Convenient method to set output directory from file path, relative to project directory. */
    var outputDirectory: String
        @OutputDirectory get() = outputDir!!.absolutePath
        set(value) {
            outputDir = project.projectDir.resolve(value)
        }

    init {
        // always consider this task out of date
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun generate() {
        logger.info("Preparing localization...")
        outputDir!!.mkdirs()

        logger.info("Writing localization...")
        write()

        logger.info("Localization done!")
    }

    /** Actual file writing process goes here. */
    abstract fun write()

    internal fun setTable(table: LocaleTable) {
        this.table = table
    }

    /** File name suffix. For example, `-en` and `-id` considering `-` is the [separator]. */
    protected fun Locale.toSuffix(separator: Char): String = buildString {
        if (this@toSuffix == defaultLocale) {
            return@buildString
        }
        append("$separator$language")
        if (country.isNotBlank()) {
            append("$separator$country")
        }
    }

    /** Delete old file, if necessary. */
    protected fun File.deleteIfExists() {
        if (exists() && delete()) {
            logger.info("Deleting old `$path`...")
        }
    }

    /** Top-level file comment with optional timestamp. Properties file already have a timestamp. */
    protected fun getFileComment(withTimestamp: Boolean): String = buildString {
        append("Generated file by locale-gradle-plugin, do not edit manually.")
        if (withTimestamp) {
            appendln()
            append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm").format(LocalDateTime.now()))
        }
    }

    /** Iterate each row, sorted if necessary. */
    protected fun forEachRow(action: (String) -> Unit) {
        var collection: Collection<String> = table.rowKeySet()
        if (sortRows) {
            collection = collection.sorted()
        }
        collection.forEach(action)
    }
}

/** Task to write properties files which will then be used as [java.util.ResourceBundle]. */
open class LocalizeJavaTask : LocalizeTask() {

    override fun write() = table.columnKeySet().forEach { locale ->
        val properties = SortedProperties()
        forEachRow { key ->
            properties[key] = table[key, locale]
        }
        val outputFile = outputDir!!.resolve("$resourceName${locale.toSuffix('_')}.properties")
        outputFile.deleteIfExists()
        outputFile.outputStream().use {
            properties.store(it, getFileComment(false))
        }
    }

    /**
     * Sorted properties that reportedly only works on Java 8.
     *
     * @see [StackOverflow](https://stackoverflow.com/a/52127284/1567541)
     */
    private class SortedProperties : Properties() {
        companion object {
            const val serialVersionUID = 1L
        }

        override val keys: MutableSet<Any>
            get() = Collections.unmodifiableSet(TreeSet(super.keys))

        override val entries: MutableSet<MutableMap.MutableEntry<Any, Any>>
            get() {
                val set1 = super.entries
                val set2 = LinkedHashSet<MutableMap.MutableEntry<Any, Any>>(set1.size)
                set1.sortedWith(Comparator<MutableMap.MutableEntry<Any, Any>> { o1, o2 ->
                    o1.key.toString().compareTo(o2.key.toString())
                }).forEach(set2::add)
                return set2
            }

        override fun keys(): Enumeration<Any> =
            Collections.enumeration(TreeSet(super.keys))
    }
}

/** Task to write Android string resources XML files. */
open class LocalizeAndroidTask : LocalizeTask() {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory
        .newInstance().apply { setAttribute("indent-number", 4) }
        .newTransformer().apply {
            setOutputProperty(OutputKeys.INDENT, "yes")
            // http://stackoverflow.com/a/18251901/3375325
            setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes")
        }

    override fun write() = table.columnKeySet().forEach { locale ->
        val doc = docBuilder.newDocument().apply { xmlStandalone = true }
        doc.appendChild(doc.createComment(getFileComment(true)))
        val resources = doc.appendChild(doc.createElement("resources"))
        forEachRow { key ->
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
