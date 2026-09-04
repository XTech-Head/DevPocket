package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class Base64UtilityTest {

    @Test
    fun `encode then decode returns original text`() {
        val original = "x-DevPocket rocks"
        val encoded = Base64Utility.encode(original)
        assertTrue(encoded is TextOpResult.Success)
        val decoded = Base64Utility.decode((encoded as TextOpResult.Success).output)
        assertTrue(decoded is TextOpResult.Success)
        assertEquals(original, (decoded as TextOpResult.Success).output)
    }

    @Test
    fun `decode invalid base64 returns error not crash`() {
        val result = Base64Utility.decode("not-valid-base64-!!!")
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `encode empty input returns error`() {
        val result = Base64Utility.encode("")
        assertTrue(result is TextOpResult.Error)
    }
}
