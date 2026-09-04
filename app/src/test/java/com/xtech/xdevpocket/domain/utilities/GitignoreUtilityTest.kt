package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GitignoreUtilityTest {

    @Test
    fun `no templates selected returns error`() {
        val result = GitignoreUtility.generate(emptyList())
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `single template includes its section header and lines`() {
        val result = GitignoreUtility.generate(listOf("android"))
        assertTrue(result is TextOpResult.Success)
        val output = (result as TextOpResult.Success).output
        assertTrue(output.contains("### Android ###"))
        assertTrue(output.contains("*.apk"))
    }

    @Test
    fun `unknown template id is ignored without crashing`() {
        val result = GitignoreUtility.generate(listOf("android", "does_not_exist"))
        assertTrue(result is TextOpResult.Success)
    }

    @Test
    fun `duplicate lines across templates are not repeated`() {
        val result = GitignoreUtility.generate(listOf("kotlin_java_gradle", "android")) as TextOpResult.Success
        val buildLineCount = result.output.lines().count { it.trim() == "build/" }
        assertEquals(1, buildLineCount)
    }

    @Test
    fun `multiple templates each keep their own header`() {
        val result = GitignoreUtility.generate(listOf("macos", "windows", "linux")) as TextOpResult.Success
        assertTrue(result.output.contains("### macOS ###"))
        assertTrue(result.output.contains("### Windows ###"))
        assertTrue(result.output.contains("### Linux ###"))
    }
}
