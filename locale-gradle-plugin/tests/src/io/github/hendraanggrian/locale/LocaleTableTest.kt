package io.github.hendraanggrian.locale

import com.google.common.collect.TreeBasedTable
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class LocaleTableTest {

    @Test fun sort() {
        val table = TreeBasedTable.create<String, String, String>()
        table.put("z", "en", "")
        table.put("d", "en", "")
        table.put("p", "en", "")
        table.put("a", "en", "")
        table.put("m", "en", "")

        assertThat(table.rowKeySet().sorted())
            .containsExactly("a", "d", "m", "p", "z")
            .inOrder()
    }
}