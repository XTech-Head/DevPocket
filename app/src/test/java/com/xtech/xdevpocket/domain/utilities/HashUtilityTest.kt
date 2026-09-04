package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HashUtilityTest {

    @Test
    fun `sha256 matches known vector for empty-safe input`() {
        val result = HashUtility.hash("hello", HashAlgorithm.SHA256)
        assertTrue(result is TextOpResult.Success)
        assertEquals(
            "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824",
            (result as TextOpResult.Success).output,
        )
    }

    @Test
    fun `md5 matches known vector`() {
        val result = HashUtility.hash("hello", HashAlgorithm.MD5)
        assertTrue(result is TextOpResult.Success)
        assertEquals("5d41402abc4b2a76b9719d911017c592", (result as TextOpResult.Success).output)
    }

    @Test
    fun `same input produces same hash`() {
        val first = HashUtility.hash("x-DevPocket", HashAlgorithm.SHA512)
        val second = HashUtility.hash("x-DevPocket", HashAlgorithm.SHA512)
        assertEquals(first, second)
    }

    @Test
    fun `empty input returns error`() {
        val result = HashUtility.hash("", HashAlgorithm.SHA256)
        assertTrue(result is TextOpResult.Error)
    }
}
