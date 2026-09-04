package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegexUtilityTest {

    @Test
    fun `finds all digit matches`() {
        val result = RegexUtility.test("\\d+", "order 12 has 345 items")
        assertTrue(result is RegexTestResult.Success)
        val matches = (result as RegexTestResult.Success).matches
        assertEquals(2, matches.size)
        assertEquals("12", matches[0].fullMatch)
        assertEquals("345", matches[1].fullMatch)
    }

    @Test
    fun `captures groups`() {
        val result = RegexUtility.test("(\\w+)@(\\w+)", "contact dev@xtechdevs")
        val matches = (result as RegexTestResult.Success).matches
        assertEquals(1, matches.size)
        assertEquals(listOf("dev", "xtechdevs"), matches[0].groups)
    }

    @Test
    fun `no matches returns empty list not error`() {
        val result = RegexUtility.test("zzz", "no matching text here")
        assertTrue(result is RegexTestResult.Success)
        assertTrue((result as RegexTestResult.Success).matches.isEmpty())
    }

    @Test
    fun `invalid pattern returns error not crash`() {
        val result = RegexUtility.test("[unclosed", "text")
        assertTrue(result is RegexTestResult.Error)
    }

    @Test
    fun `empty pattern returns error`() {
        assertTrue(RegexUtility.test("", "text") is RegexTestResult.Error)
    }
}
