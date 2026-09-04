package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class JwtUtilityTest {

    // header: {"alg":"HS256","typ":"JWT"}  payload: {"sub":"1234567890","name":"John Doe","iat":1516239022}
    private val sampleJwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
            "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

    @Test
    fun `decode extracts header and payload`() {
        val result = JwtUtility.decode(sampleJwt)
        assertTrue(result is JwtResult.Success)
        val decoded = (result as JwtResult.Success).decoded
        assertEquals("HS256", decoded.algorithm)
        assertEquals("JWT", decoded.tokenType)
        assertTrue(decoded.payloadJson.contains("John Doe"))
    }

    @Test
    fun `decode rejects tokens without three segments`() {
        val result = JwtUtility.decode("not.a.valid.jwt.token")
        assertTrue(result is JwtResult.Error)
    }

    @Test
    fun `decode rejects empty input`() {
        val result = JwtUtility.decode("")
        assertTrue(result is JwtResult.Error)
    }
}
