package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorConverterUtilityTest {

    @Test
    fun `parses short hex and expands it`() {
        val result = ColorConverterUtility.convert("#0f0")
        assertTrue(result is ColorResult.Success)
        assertEquals("#00FF00", (result as ColorResult.Success).conversion.hex)
    }

    @Test
    fun `parses rgb function notation`() {
        val result = ColorConverterUtility.convert("rgb(0, 217, 192)")
        assertTrue(result is ColorResult.Success)
        assertEquals("#00D9C0", (result as ColorResult.Success).conversion.hex)
    }

    @Test
    fun `hex to hsl round trip stays close to original hue`() {
        val result = ColorConverterUtility.convert("#00D9C0") as ColorResult.Success
        assertTrue(result.conversion.hsl.startsWith("hsl(17"))
    }

    @Test
    fun `rejects unparseable input`() {
        val result = ColorConverterUtility.convert("not-a-color")
        assertTrue(result is ColorResult.Error)
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(ColorConverterUtility.convert("") is ColorResult.Error)
    }
}
