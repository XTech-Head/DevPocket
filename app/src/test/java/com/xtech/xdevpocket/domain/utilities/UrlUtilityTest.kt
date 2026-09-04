package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UrlUtilityTest {

    @Test
    fun `encode escapes spaces and special characters`() {
        val result = UrlUtility.encode("hello world/x?y=1")
        assertTrue(result is TextOpResult.Success)
        assertEquals("hello+world%2Fx%3Fy%3D1", (result as TextOpResult.Success).output)
    }

    @Test
    fun `encode then decode round-trips`() {
        val original = "a=1&b=hello world"
        val encoded = (UrlUtility.encode(original) as TextOpResult.Success).output
        val decoded = UrlUtility.decode(encoded)
        assertEquals(original, (decoded as TextOpResult.Success).output)
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(UrlUtility.encode("") is TextOpResult.Error)
        assertTrue(UrlUtility.decode("") is TextOpResult.Error)
    }
}
