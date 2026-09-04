package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertTrue
import org.junit.Test

class XmlFormatterUtilityTest {

    @Test
    fun `format pretty-prints valid xml`() {
        val result = XmlFormatterUtility.format("<root><item>value</item></root>")
        assertTrue(result is TextOpResult.Success)
        assertTrue((result as TextOpResult.Success).output.contains("\n"))
    }

    @Test
    fun `minify strips whitespace between tags`() {
        val result = XmlFormatterUtility.minify("<root>\n  <item>value</item>\n</root>")
        assertTrue(result is TextOpResult.Success)
        assertTrue(!(result as TextOpResult.Success).output.contains("\n"))
    }

    @Test
    fun `validate rejects malformed xml`() {
        val result = XmlFormatterUtility.validate("<root><item></root>")
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(XmlFormatterUtility.format("") is TextOpResult.Error)
    }
}
