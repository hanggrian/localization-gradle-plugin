package com.hendraanggrian.locale

import org.gradle.api.Action

interface LocaleRowBuilder {

    /** Opening closure to modify content of [key]. */
    fun row(key: String, action: Action<LocaleColumnBuilder>)

    /** Alias of [row] for Gradle Kotlin DSL. */
    operator fun String.invoke(action: LocaleColumnBuilder.() -> Unit): Unit =
        row(this, action)
}
