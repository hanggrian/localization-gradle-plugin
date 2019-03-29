package com.hendraanggrian.locale

import org.junit.Test

class LocaleExtensionTest {

    @Test
    fun test() {
        val ext = LocaleExtension()
        ext.apply {
            "about" { a ->
                a.en = ""
                val xz = a.en
            }
        }
    }
}