package com.hendraanggrian.locale

import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleExtensionTest {

    @Test
    fun test() {
        val bahasa = Locale("id")
        val ext = LocaleExtension()
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
        assertEquals("Home", ext.table["home", Locale.ENGLISH])
        assertEquals("Beranda", ext.table["home", bahasa])
        assertEquals("About", ext.table["about", Locale.ENGLISH])
        assertEquals("Tentang", ext.table["about", bahasa])
    }
}