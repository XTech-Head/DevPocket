package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertTrue
import org.junit.Test

class CronUtilityTest {

    @Test
    fun `describes every-N-minutes pattern`() {
        val result = CronUtility.describe("*/5 * * * *")
        assertTrue(result is CronResult.Success)
        assertTrue((result as CronResult.Success).description.contains("Every 5 minutes"))
    }

    @Test
    fun `describes fixed time pattern`() {
        val result = CronUtility.describe("0 9 * * 1")
        assertTrue(result is CronResult.Success)
        val description = (result as CronResult.Success).description
        assertTrue(description.contains("09:00"))
        assertTrue(description.contains("Monday"))
    }

    @Test
    fun `rejects expressions without five fields`() {
        val result = CronUtility.describe("* * * *")
        assertTrue(result is CronResult.Error)
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(CronUtility.describe("") is CronResult.Error)
    }
}
