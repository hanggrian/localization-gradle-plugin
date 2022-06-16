package com.hendraanggrian.localization.internal

import com.hendraanggrian.localization.LocaleTable
import com.hendraanggrian.localization.LocalizationTextBuilder
import com.hendraanggrian.localization.LocalizationTextScope
import com.hendraanggrian.localization.LocalizeSpec
import com.hendraanggrian.localization.forEachLocale
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.property
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/** Non-platform specific locale writer task. */
abstract class AbstractLocalizeTask : DefaultTask(), LocalizeSpec {

    @Internal
    final override fun getLogger(): Logger = project.logger

    @Internal
    val defaultLocale: Property<Locale> = project.objects.property()

    @Input
    final override val table: Property<LocaleTable> = project.objects.property()

    @Input
    final override val resourceName: Property<String> = project.objects.property()

    /**
     * Where localization files will be generated to.
     * Default is `$projectDir/src/main/resources`.
     */
    @OutputDirectory
    val outputDirectory: Property<File> = project.objects.property<File>()
        .convention(project.projectDir.resolve("src/main/resources"))

    private val textBuilder = LocalizationTextBuilder(table)

    final override fun text(key: String, configuration: Action<LocalizationTextScope>) {
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

        require(resourceName.get().isNotBlank()) { "Empty file resource name" }

        logger.info("  Locale table column = ${table.get().columnKeySet().size}, row = ${table.get().rowKeySet().size}")
        table.get().forEachLocale { column, locale -> onGenerateLocale(column, locale) }
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
}
