package com.xtech.xdevpocket.domain.utilities

import org.junit.Assert.assertTrue
import org.junit.Test

class HttpRequestBuilderUtilityTest {

    @Test
    fun `builds curl command with method and headers`() {
        val spec = HttpRequestSpec(
            method = HttpMethod.POST,
            url = "https://api.example.com/users",
            headers = listOf(HttpHeader("Content-Type", "application/json")),
            body = "{\"name\":\"test\"}",
        )
        val result = HttpRequestBuilderUtility.buildCurl(spec)
        assertTrue(result is TextOpResult.Success)
        val output = (result as TextOpResult.Success).output
        assertTrue(output.contains("-X POST"))
        assertTrue(output.contains("Content-Type: application/json"))
        assertTrue(output.contains("api.example.com/users"))
    }

    @Test
    fun `builds raw http request line`() {
        val spec = HttpRequestSpec(method = HttpMethod.GET, url = "https://api.example.com/users?limit=10")
        val result = HttpRequestBuilderUtility.buildRawHttp(spec)
        assertTrue(result is TextOpResult.Success)
        val output = (result as TextOpResult.Success).output
        assertTrue(output.startsWith("GET /users"))
        assertTrue(output.contains("Host: api.example.com"))
    }

    @Test
    fun `blank url returns error`() {
        val spec = HttpRequestSpec(url = "")
        assertTrue(HttpRequestBuilderUtility.buildCurl(spec) is TextOpResult.Error)
    }
}
