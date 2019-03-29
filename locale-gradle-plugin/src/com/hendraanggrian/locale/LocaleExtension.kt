package com.hendraanggrian.locale

import org.gradle.api.Action

open class LocaleExtension {

    private val map = mutableMapOf<String, Pair<String, String>>()

    operator fun String.invoke(action: Action<LocaleBuilder>) {
        object : LocaleBuilder {
            override fun en(value: String) = map.set("en", this@invoke to value)
            override fun de(value: String) = map.set("de", this@invoke to value)
        }.also { action.execute(it) }
    }
}