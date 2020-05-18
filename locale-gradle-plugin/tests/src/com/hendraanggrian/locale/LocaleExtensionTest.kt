package com.hendraanggrian.locale

import com.google.common.truth.Truth.assertThat
import java.io.File
import kotlin.test.Test

class LocaleExtensionTest {

    @Test fun test() {
        val ext = LocaleExtension(File(""))
        ext.run {
            "home" {
                en = "Home"
                en_US = "Home2"
                id = "Beranda"
            }
            "about" {
                en = "About"
                en_US = "About2"
                id = "Tentang"
            }
        }
        val table = ext.table
        assertThat(table).containsCell("home", "en", "Home")
        assertThat(table).containsCell("home", "en-US", "Home2")
        assertThat(table).containsCell("home", "id", "Beranda")
        assertThat(table).containsCell("about", "en", "About")
        assertThat(table).containsCell("about", "en-US", "About2")
        assertThat(table).containsCell("about", "id", "Tentang")
    }
}