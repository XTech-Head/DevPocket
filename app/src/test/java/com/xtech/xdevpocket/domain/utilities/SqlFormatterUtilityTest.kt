package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertTrue
import org.junit.Test

class SqlFormatterUtilityTest {

    @Test
    fun `uppercases keywords and breaks clauses`() {
        val result = SqlFormatterUtility.format("select id, name from users where active = 1")
        assertTrue(result is TextOpResult.Success)
        val output = (result as TextOpResult.Success).output
        assertTrue(output.contains("SELECT"))
        assertTrue(output.contains("FROM"))
        assertTrue(output.contains("WHERE"))
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(SqlFormatterUtility.format("") is TextOpResult.Error)
    }

    @Test
    fun `minify collapses whitespace`() {
        val result = SqlFormatterUtility.minify("SELECT  *\nFROM   users")
        assertTrue(result is TextOpResult.Success)
        assertTrue(!(result as TextOpResult.Success).output.contains("\n"))
    }
}
