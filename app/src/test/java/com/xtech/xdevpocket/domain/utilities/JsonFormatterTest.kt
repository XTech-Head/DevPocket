package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JsonFormatterTest {

    @Test
    fun `format pretty-prints valid object json`() {
        val result = JsonFormatter.format("""{"name":"x-DevPocket","offline":true}""")
        assertTrue(result is JsonResult.Success)
        val output = (result as JsonResult.Success).output
        assertTrue(output.contains("\"name\": \"x-DevPocket\""))
    }

    @Test
    fun `format handles arrays`() {
        val result = JsonFormatter.format("[1,2,3]")
        assertTrue(result is JsonResult.Success)
    }

    @Test
    fun `format returns error for malformed json`() {
        val result = JsonFormatter.format("{name: x-DevPocket}")
        assertTrue(result is JsonResult.Error)
    }

    @Test
    fun `format returns error for empty input`() {
        val result = JsonFormatter.format("")
        assertTrue(result is JsonResult.Error)
    }

    @Test
    fun `minify strips whitespace`() {
        val result = JsonFormatter.minify("{\n  \"a\": 1\n}")
        assertTrue(result is JsonResult.Success)
        assertEquals("{\"a\":1}", (result as JsonResult.Success).output)
    }

    @Test
    fun `validate succeeds on well formed json`() {
        val result = JsonFormatter.validate("""{"ok":true}""")
        assertTrue(result is JsonResult.Success)
    }
}
