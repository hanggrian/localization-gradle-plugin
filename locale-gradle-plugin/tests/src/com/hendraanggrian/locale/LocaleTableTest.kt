package com.hendraanggrian.locale

import com.google.common.truth.Truth
import java.util.Locale
import kotlin.test.Test

class LocaleTableTest {

    @Test fun sort() {
        val table = localeTableOf()
        val locale = Locale.ENGLISH
        table.put("z", locale, "")
        table.put("d", locale, "")
        table.put("p", locale, "")
        table.put("a", locale, "")
        table.put("m", locale, "")

        Truth.assertThat(table.rowKeySet().sorted())
            .containsExactly("a", "d", "m", "p", "z")
            .inOrder()
    }
}