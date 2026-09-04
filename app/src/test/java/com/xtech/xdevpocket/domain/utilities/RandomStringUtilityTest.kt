package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RandomStringUtilityTest {

    @Test
    fun `generates string of requested length`() {
        val result = RandomStringUtility.generate(RandomStringUtility.Options(length = 24))
        assertTrue(result is TextOpResult.Success)
        assertEquals(24, (result as TextOpResult.Success).output.length)
    }

    @Test
    fun `no character sets selected returns error`() {
        val options = RandomStringUtility.Options(
            includeUpper = false,
            includeLower = false,
            includeDigits = false,
            includeSymbols = false,
        )
        val result = RandomStringUtility.generate(options)
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `digits-only output contains only digits`() {
        val options = RandomStringUtility.Options(
            length = 40,
            includeUpper = false,
            includeLower = false,
            includeDigits = true,
            includeSymbols = false,
        )
        val result = RandomStringUtility.generate(options) as TextOpResult.Success
        assertTrue(result.output.all { it.isDigit() })
    }
}
