package com.hendraanggrian.locale

import com.opencsv.CSVReader
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class CsvTest {

    @Test fun importCSV() {
        val result = CSVReader(File("tests/res/test.csv").inputStream().bufferedReader()).readAll()
        val headers = result.first().first().split(';')
        assertEquals("key", headers.first())
        result.drop(1).forEach {
            println()
            println(it)
            it.first().split(';').forEach(::println)
        }
    }
}