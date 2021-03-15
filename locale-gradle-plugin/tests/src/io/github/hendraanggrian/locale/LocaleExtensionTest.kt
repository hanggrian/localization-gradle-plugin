package io.github.hendraanggrian.locale

import com.google.common.truth.Truth.assertThat
import org.gradle.api.logging.Logging
import kotlin.test.Test

class LocaleExtensionTest {

    @Test fun test() {
        val ext = LocaleExtension(FakeDir, Logging.getLogger(LocaleExtensionTest::class.java))
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