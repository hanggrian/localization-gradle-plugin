package com.hendraanggrian.locale

import java.io.File
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

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
        assertEquals("Home", table["home", Locale.ENGLISH])
        assertEquals("Beranda", table["home", bahasa])
        assertEquals("About", table["about", Locale.ENGLISH])
        assertEquals("Tentang", table["about", bahasa])
    }
}