package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertTrue
import org.junit.Test

class TimestampUtilityTest {

    @Test
    fun `converts unix seconds to readable date`() {
        val result = TimestampUtility.convertUnixToDate("1704067200") // 2024-01-01 00:00:00 UTC
        assertTrue(result is TextOpResult.Success)
        assertTrue((result as TextOpResult.Success).output.contains("2024"))
    }

    @Test
    fun `converts unix millis to readable date`() {
        val result = TimestampUtility.convertUnixToDate("1704067200000")
        assertTrue(result is TextOpResult.Success)
        assertTrue((result as TextOpResult.Success).output.contains("2024"))
    }

    @Test
    fun `rejects non-numeric input`() {
        val result = TimestampUtility.convertUnixToDate("not-a-timestamp")
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `converts date string to unix seconds`() {
        val result = TimestampUtility.convertDateToUnix("2024-01-01")
        assertTrue(result is TextOpResult.Success)
        val seconds = (result as TextOpResult.Success).output.toLong()
        assertTrue(seconds > 0)
    }

    @Test
    fun `rejects unparseable date string`() {
        val result = TimestampUtility.convertDateToUnix("definitely not a date")
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `empty input returns error for both directions`() {
        assertTrue(TimestampUtility.convertUnixToDate("") is TextOpResult.Error)
        assertTrue(TimestampUtility.convertDateToUnix("") is TextOpResult.Error)
    }
}
