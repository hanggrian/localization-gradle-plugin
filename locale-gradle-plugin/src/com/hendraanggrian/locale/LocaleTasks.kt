package com.hendraanggrian.locale

import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import java.util.Properties

abstract class LocaleTask : DefaultTask() {

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
}

class AndroidLocaleTask : LocaleTask() {

    override fun write() {
    }
}

class JavaLocaleTask : LocaleTask() {

    override fun write() {
        val properties = Properties()
    }
}