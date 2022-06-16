package com.hendraanggrian.localization.internal

import com.hendraanggrian.localization.LocaleTable
import com.hendraanggrian.localization.LocalizationExtension
import com.hendraanggrian.localization.LocalizationTextBuilder
import com.hendraanggrian.localization.LocalizationTextScope
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.property
import java.util.*

/** Gradle extension to configure localization, any changes made here will take affect in [AbstractLocalizeTask]. */
open class DefaultLocalizationExtension(private val project: Project) : LocalizationExtension {
    final override fun getLogger(): Logger = project.logger

    final override val table: Property<LocaleTable> = project.objects.property<LocaleTable>()
        .convention(LocaleTable.create())

    final override val resourceName: Property<String> = project.objects.property<String>()
        .convention("strings")

    final override val defaultLocale: Property<Locale> = project.objects.property<Locale>()
        .convention(Locale.ENGLISH)

    private val textBuilder = LocalizationTextBuilder(table)

    final override fun text(key: String, configuration: Action<LocalizationTextScope>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }
}
