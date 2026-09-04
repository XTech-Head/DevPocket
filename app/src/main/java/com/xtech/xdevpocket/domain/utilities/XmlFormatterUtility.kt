package com.xtech.xdevpocket.domain.utilities

import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/** Formats, minifies and validates XML using the standard javax.xml toolkit — no external dependency, fully offline. */
object XmlFormatterUtility {

    private fun parse(input: String): Node {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = true
        val builder = factory.newDocumentBuilder()
        return builder.parse(InputSource(StringReader(input))).documentElement
    }

    fun format(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")
        return try {
            val root = parse(input)
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
            val writer = StringWriter()
            transformer.transform(DOMSource(root), StreamResult(writer))
            TextOpResult.Success(writer.toString().trim())
        } catch (e: Exception) {
            TextOpResult.Error("Invalid XML\n\n${e.message ?: "Malformed document."}")
        }
    }

    fun minify(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")
        return try {
            val root = parse(input)
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "no")
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            val writer = StringWriter()
            transformer.transform(DOMSource(root), StreamResult(writer))
            val compact = writer.toString().replace(Regex(">\\s+<"), "><").trim()
            TextOpResult.Success(compact)
        } catch (e: Exception) {
            TextOpResult.Error("Invalid XML\n\n${e.message ?: "Malformed document."}")
        }
    }

    fun validate(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")
        return try {
            parse(input)
            TextOpResult.Success("Valid XML")
        } catch (e: Exception) {
            TextOpResult.Error("Invalid XML\n\n${e.message ?: "Malformed document."}")
        }
    }
}
