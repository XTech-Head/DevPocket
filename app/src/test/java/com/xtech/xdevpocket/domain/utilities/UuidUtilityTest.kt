package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UuidUtilityTest {

    private val uuidPattern = Regex(
        "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"
    )

    @Test
    fun `generate produces a valid v4 uuid`() {
        val id = UuidUtility.generate()
        assertTrue(uuidPattern.matches(id))
    }

    @Test
    fun `generateMultiple returns requested count`() {
        val ids = UuidUtility.generateMultiple(10)
        assertEquals(10, ids.size)
    }

    @Test
    fun `generateMultiple values are unique`() {
        val ids = UuidUtility.generateMultiple(20)
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun `generateMultiple coerces out-of-range count`() {
        val ids = UuidUtility.generateMultiple(0)
        assertEquals(1, ids.size)
    }

    @Test
    fun `two generated ids are different`() {
        assertNotEquals(UuidUtility.generate(), UuidUtility.generate())
    }
}
