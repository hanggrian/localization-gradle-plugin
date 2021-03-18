@file:Suppress("UnstableApiUsage")

package io.github.hendraanggrian.locale

import com.google.common.collect.Ordering
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.FileOutputStream
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

/** Non-platform specific locale writer task. */
sealed class AbstractLocalizeTask : DefaultTask(), LocaleConfiguration, LocaleTableBuilder {

    @Internal
    override fun getLogger(): Logger = super.getLogger()

    @Input
    override val resourceName: Property<String> = project.objects.property()

    @Optional
    @Input
    override val defaultLocale: Property<Locale> = project.objects.property()

    @Input
    override val sortValues: Property<Boolean> = project.objects.property()

    @OutputDirectory
    override val outputDirectory: DirectoryProperty = project.objects.directoryProperty()

    @get:Internal
    internal val table: LocaleTable = LocaleTable.create()
    private val textBuilder = LocaleTextBuilderImpl(table)

    init {
        outputs.upToDateWhen { false } // always consider this task out of date
    }

    override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }

    @TaskAction
    fun generate() {
        logger.info(
            "Generating resources with ${
            when {
                defaultLocale.isPresent -> "default locale '${defaultLocale.get()}'"
                else -> "no default locale"
            }
            }:"
        )

        require(resourceName.get().isNotBlank()) { "Empty file resource name." }

        logger.info("  Locale table column = ${table.columnKeySet().size}, row = ${table.rowKeySet().size}")
        table.forEachLocale { column, locale -> onGenerateLocale(column, locale) }
        logger.info("  All resources generated")
    }

    /** Where the resources are generated. */
    abstract fun onGenerateLocale(column: String, locale: Locale)

    /** File name suffix. For example, `-en` and `-id` considering `-` is the [separator]. */
    protected fun getSuffix(locale: Locale, separator: Char): String = buildString {
        if (defaultLocale.isPresent && locale == defaultLocale.get()) {
            return@buildString
        }
        append("$separator${locale.language}")
        if (locale.country.isNotEmpty()) {
            append("$separator${locale.country}")
        }
    }

    /** Top-level file comment with optional timestamp. Properties file already have a timestamp. */
    protected fun getFileComment(withTimestamp: Boolean): String = buildString {
        append("Generated file by locale-gradle-plugin, do not edit manually!")
        if (withTimestamp) {
            @Suppress("DEPRECATION")
            appendln()
            append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm").format(LocalDateTime.now()))
        }
    }

    /** Iterate each row, sorted if necessary. */
    protected fun forEachRow(action: (String) -> Unit) {
        var collection: Collection<String> = table.rowKeySet()
        if (sortValues.get()) {
            collection = collection.sorted()
        }
        collection.forEach(action)
    }

    /** Create individual file, reporting the result to logger. */
    protected fun File.write(action: (FileOutputStream) -> Unit) {
        if (exists()) {
            logger.info("  Existing resource '$name' deleted")
            delete()
        }
        outputStream().use { action(it) }
        logger.info(
            "  ${
            when {
                exists() -> "Resource '$name' created"
                else -> "Failed to create resource '$name'"
            }
            }"
        )
    }

    protected val outputDir: File @Internal get() = outputDirectory.get().asFile
}

/** Task to write properties files which will then be used as [java.util.ResourceBundle]. */
open class LocalizeJvmTask : AbstractLocalizeTask() {

    final override fun onGenerateLocale(column: String, locale: Locale) {
        val properties = SortedProperties()
        forEachRow { row -> properties[row] = table[row, column] }

        outputDir.mkdirs()
        val outputFile = outputDir
            .resolve("${resourceName.get()}${getSuffix(locale, '_')}.properties")
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

/** Task to write Android string resources XML files. */
open class LocalizeAndroidTask : AbstractLocalizeTask() {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory
        .newInstance().apply { setAttribute("indent-number", 4) }
        .newTransformer().apply {
            setOutputProperty(OutputKeys.INDENT, "yes")
            // http://stackoverflow.com/a/18251901/3375325
            setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes")
        }

    final override fun onGenerateLocale(column: String, locale: Locale) {
        val doc = docBuilder.newDocument().apply { xmlStandalone = true }
        doc.appendChild(doc.createComment(getFileComment(true)))
        val resources = doc.appendChild(doc.createElement("resources"))
        forEachRow { row ->
            val s = doc.createElement("string")
            s.setAttribute("name", row)
            s.appendChild(doc.createTextNode(table[row, column]))
            resources.appendChild(s)
        }

        outputDir.mkdirs()
        val innerOutputDir = outputDir.resolve("values${getSuffix(locale, '-')}")
        innerOutputDir.mkdirs()
        val outputFile = innerOutputDir.resolve("${resourceName.get()}.xml")
        outputFile.write { transformer.transform(DOMSource(doc), StreamResult(it)) }
    }
}
