package io.github.hendraanggrian.locale

class LocaleTableBuilderTest {

    /*private val table = TreeBasedTable.create<String, String, String>()
    private val textBuilder = LocaleTextBuilderImpl(table)
    private val builder = object : LocaleTableBuilder {
        override val projectDir: File get() = FakeDir
        override val internalLogger: Logger get() = Logging.getLogger(LocaleTableBuilderTest::class.java)
        override fun text(key: String, configuration: Action<LocaleTextBuilder>) {
            textBuilder.currentRow = key
            configuration(textBuilder)
        }
    }

    @Test fun importCSV() {
        builder.importCSV(File("tests/res/test.csv"))
        assertThat(table).containsCell("about", "en", "About")
        assertThat(table).containsCell("about", "en-US", "About2")
        assertThat(table).containsCell("about", "id", "Tentang")
        assertThat(table).containsCell("home", "en", "Home")
        assertThat(table).containsCell("home", "en-US", "Home2")
        assertThat(table).containsCell("home", "id", "Beranda")
    }*/
}