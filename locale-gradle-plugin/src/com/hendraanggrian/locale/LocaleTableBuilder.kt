package com.hendraanggrian.locale

import org.gradle.api.Action

interface LocaleTableBuilder {

    /** Opening closure to modify content of [key]. */
    fun configure(key: String, action: Action<LocaleTableValueBuilder>)

    /** Alias of [configure] for Gradle Kotlin DSL. */
    operator fun String.invoke(action: LocaleTableValueBuilder.() -> Unit) =
        configure(this, action)
}
