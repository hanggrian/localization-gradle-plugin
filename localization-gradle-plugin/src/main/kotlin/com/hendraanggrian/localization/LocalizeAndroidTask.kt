package com.hendraanggrian.localization

import com.hendraanggrian.localization.internal.AbstractLocalizeTask
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * Task to run when `localizeAndroid` command is executed. It writes Android string resources XML
 * files.
 */
open class LocalizeAndroidTask : AbstractLocalizeTask() {
    private val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    private val transformer = TransformerFactory
        .newInstance().apply { setAttribute("indent-number", 4) }
        .newTransformer().apply {
            setOutputProperty(OutputKeys.INDENT, "yes")
            // http://stackoverflow.com/a/18251901/3375325
            setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes")
        }

    final override fun onGenerateLocale(column: String, locale: Locale) {
        val doc = docBuilder.newDocument().apply { xmlStandalone = true }
        doc.appendChild(doc.createComment(getFileComment(true)))
        val resources = doc.appendChild(doc.createElement("resources"))
        table.get().rowKeySet().forEach { row ->
            val s = doc.createElement("string")
            s.setAttribute("name", row)
            s.appendChild(doc.createTextNode(table.get()[row, column]))
            resources.appendChild(s)
        }

        outputDirectory.get().mkdirs()
        val innerOutputDirectory = outputDirectory.get().resolve("values${getSuffix(locale, '-')}")
        innerOutputDirectory.mkdirs()
        val outputFile = innerOutputDirectory
            .resolve("${resourceName.get()}.xml")
        outputFile.write { transformer.transform(DOMSource(doc), StreamResult(it)) }
    }
}
