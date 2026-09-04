package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommitMessageUtilityTest {

    @Test
    fun `blank subject returns error`() {
        val result = CommitMessageUtility.build(CommitMessageUtility.Options(subject = "  "))
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `subject over 72 chars returns error`() {
        val longSubject = "a".repeat(73)
        val result = CommitMessageUtility.build(CommitMessageUtility.Options(subject = longSubject))
        assertTrue(result is TextOpResult.Error)
    }

    @Test
    fun `simple feat commit formats header only`() {
        val result = CommitMessageUtility.build(
            CommitMessageUtility.Options(type = CommitMessageUtility.CommitType.FEAT, subject = "add dark mode"),
        )
        assertTrue(result is TextOpResult.Success)
        assertEquals("feat: add dark mode", (result as TextOpResult.Success).output)
    }

    @Test
    fun `scope is wrapped in parentheses`() {
        val result = CommitMessageUtility.build(
            CommitMessageUtility.Options(
                type = CommitMessageUtility.CommitType.FIX,
                scope = "jwt",
                subject = "handle malformed tokens",
            ),
        ) as TextOpResult.Success
        assertEquals("fix(jwt): handle malformed tokens", result.output)
    }

    @Test
    fun `breaking change adds bang and footer`() {
        val result = CommitMessageUtility.build(
            CommitMessageUtility.Options(
                type = CommitMessageUtility.CommitType.FEAT,
                subject = "drop legacy API",
                breakingChange = "removes the v1 endpoints",
            ),
        ) as TextOpResult.Success
        assertTrue(result.output.startsWith("feat!: drop legacy API"))
        assertTrue(result.output.contains("BREAKING CHANGE: removes the v1 endpoints"))
    }

    @Test
    fun `body and issue refs appear in order after header`() {
        val result = CommitMessageUtility.build(
            CommitMessageUtility.Options(
                type = CommitMessageUtility.CommitType.FIX,
                subject = "correct timezone offset",
                body = "The converter assumed UTC even when a zone was supplied.",
                issueRefs = "Closes #42",
            ),
        ) as TextOpResult.Success
        val lines = result.output.lines()
        assertEquals("fix: correct timezone offset", lines[0])
        assertTrue(result.output.contains("The converter assumed UTC"))
        assertTrue(result.output.trimEnd().endsWith("Closes #42"))
    }
}
