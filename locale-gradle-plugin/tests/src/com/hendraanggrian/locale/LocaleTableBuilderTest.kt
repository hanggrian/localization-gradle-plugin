package com.hendraanggrian.locale

import com.google.common.truth.Truth.assertThat
import org.gradle.api.Action
import org.gradle.kotlin.dsl.invoke
import java.io.File
import java.util.Locale
import kotlin.test.Test

class LocaleTableBuilderTest {
    private companion object {
        val EN = Locale("en")
        val EN_US = Locale("en", "US")
        val IN = Locale("in")
    }

    private val table = localeTableOf()
    private val textBuilder = LocaleTextBuilderImpl(table)
    private val builder = object : LocaleTableBuilder {
        override val projectDir: File get() = File("")
        override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
            textBuilder.currentRow = key
            configuration(textBuilder)
        }
    }

    @Test fun importCSV() {
        builder.importCSV(File("tests/res/test.csv"))
        assertThat(table).containsCell("about", EN, "About")
        //assertThat(table).containsCell("about", EN_US, "About2")
        assertThat(table).containsCell("about", IN, "Tentang")
        assertThat(table).containsCell("home", EN, "Home")
        //assertThat(table).containsCell("home", EN_US, "Home2")
        assertThat(table).containsCell("home", IN, "Beranda")
    }
}