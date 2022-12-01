package com.hendraanggrian.localization.internal

import com.hendraanggrian.localization.LocaleTable
import com.hendraanggrian.localization.LocalizationExtension
import com.hendraanggrian.localization.LocalizationTextBuilder
import com.hendraanggrian.localization.LocalizationTextScope
import org.gradle.api.Action
import org.gradle.api.logging.Logger
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.property
import java.util.*

/**
 * Gradle extension to configure localization, any changes made here will take effect
 * in [AbstractLocalizeTask].
 */
open class DefaultLocalizationExtension(objects: ObjectFactory, private val logger: Logger) :
    LocalizationExtension {
    final override fun getLogger(): Logger = logger

    final override val table: Property<LocaleTable> = objects.property<LocaleTable>()
        .convention(LocaleTable.create())

    final override val resourceName: Property<String> = objects.property<String>()
        .convention("strings")

    final override val defaultLocale: Property<Locale> = objects.property<Locale>()
        .convention(Locale.ENGLISH)

    private val textBuilder = LocalizationTextBuilder(table)

    final override fun text(key: String, configuration: Action<in LocalizationTextScope>) {
        textBuilder.currentRow = key
        configuration(textBuilder)
    }
}
