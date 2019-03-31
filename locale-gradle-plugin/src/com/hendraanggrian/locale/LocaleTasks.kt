package com.hendraanggrian.locale

import com.google.common.collect.RowSortedTable
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.util.Properties

abstract class LocaleTask : DefaultTask() {

    internal lateinit var table: RowSortedTable<String, String, String>

    @Input lateinit var localeName: String

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
        logger.log(LogLevel.INFO, "Deleting old localization")
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        logger.log(LogLevel.INFO, "Writing localization")
        write()
    }

    abstract fun write()

    abstract fun getFileName(locale: String): String
}

open class JavaLocaleTask : LocaleTask() {

    override fun write() = table.columnKeySet().forEach { locale ->
        val properties = Properties()
        table.rowKeySet().forEach { key ->
            properties[key] = table.get(key, locale)
        }
        outputDir.resolve(getFileName(locale)).outputStream().use {
            properties.store(it, null)
        }
    }

    override fun getFileName(locale: String): String = "${localeName}_$locale.properties"
}

open class AndroidLocaleTask : LocaleTask() {

    override fun write() = table.columnKeySet().forEach { locale ->

    }

    override fun getFileName(locale: String): String = "$localeName-$locale.xml"
}