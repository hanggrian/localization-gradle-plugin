package com.hendraanggrian.locale

import com.google.common.truth.Truth.assertThat
import java.io.File
import java.util.Locale
import kotlin.test.Test

class LocaleExtensionTest {

    @Test fun test() {
        val bahasa = Locale("id")
        val ext = LocaleExtension(File(""))
        ext.run {
            "home" {
                en = "Home"
                id = "Beranda"
            }
            "about" {
                en = "About"
                id = "Tentang"
            }
        }
        val table = ext.table
        assertThat(table).containsCell("home", Locale.ENGLISH, "Home")
        assertThat(table).containsCell("home", bahasa, "Beranda")
        assertThat(table).containsCell("about", Locale.ENGLISH, "About")
        assertThat(table).containsCell("about", bahasa, "Tentang")
    }
}