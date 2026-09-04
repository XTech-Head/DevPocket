package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CaseConverterUtilityTest {

    @Test
    fun `converts to camelCase`() {
        val result = CaseConverterUtility.convert("hello world example", CaseConverterUtility.CaseType.CAMEL)
        assertEquals("helloWorldExample", (result as TextOpResult.Success).output)
    }

    @Test
    fun `converts to snake_case`() {
        val result = CaseConverterUtility.convert("Hello World", CaseConverterUtility.CaseType.SNAKE)
        assertEquals("hello_world", (result as TextOpResult.Success).output)
    }

    @Test
    fun `converts to kebab-case`() {
        val result = CaseConverterUtility.convert("Hello World", CaseConverterUtility.CaseType.KEBAB)
        assertEquals("hello-world", (result as TextOpResult.Success).output)
    }

    @Test
    fun `converts camelCase input to CONSTANT_CASE`() {
        val result = CaseConverterUtility.convert("helloWorldExample", CaseConverterUtility.CaseType.CONSTANT)
        assertEquals("HELLO_WORLD_EXAMPLE", (result as TextOpResult.Success).output)
    }

    @Test
    fun `converts to PascalCase`() {
        val result = CaseConverterUtility.convert("hello_world", CaseConverterUtility.CaseType.PASCAL)
        assertEquals("HelloWorld", (result as TextOpResult.Success).output)
    }

    @Test
    fun `empty input returns error`() {
        assertTrue(CaseConverterUtility.convert("", CaseConverterUtility.CaseType.CAMEL) is TextOpResult.Error)
    }
}
