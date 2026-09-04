package com.xtech.xdevpocket.domain.utilities

enum class HttpMethod { GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS }

data class HttpHeader(val key: String, val value: String)

data class HttpRequestSpec(
    val method: HttpMethod = HttpMethod.GET,
    val url: String = "",
    val headers: List<HttpHeader> = emptyList(),
    val body: String = "",
)

/**
 * Builds request text (raw HTTP and curl) from a spec. This tool never sends
 * anything over the network — it only formats what you'd send, offline.
 */
object HttpRequestBuilderUtility {

    fun buildCurl(spec: HttpRequestSpec): TextOpResult {
        if (spec.url.isBlank()) return TextOpResult.Error("Enter a URL.")
        val sb = StringBuilder("curl -X ${spec.method.name}")
        spec.headers.filter { it.key.isNotBlank() }.forEach { h ->
            sb.append(" \\\n  -H \"${h.key}: ${h.value}\"")
        }
        if (spec.body.isNotBlank() && spec.method != HttpMethod.GET && spec.method != HttpMethod.HEAD) {
            val escaped = spec.body.replace("\"", "\\\"")
            sb.append(" \\\n  -d \"$escaped\"")
        }
        sb.append(" \\\n  \"${spec.url}\"")
        return TextOpResult.Success(sb.toString())
    }

    fun buildRawHttp(spec: HttpRequestSpec): TextOpResult {
        if (spec.url.isBlank()) return TextOpResult.Error("Enter a URL.")
        val path = runCatching { java.net.URI(spec.url) }.getOrNull()
        val requestLine = "${spec.method.name} ${path?.rawPath?.ifBlank { "/" } ?: "/"} HTTP/1.1"
        val host = path?.host ?: spec.url

        val sb = StringBuilder(requestLine).append("\n")
        sb.append("Host: $host\n")
        spec.headers.filter { it.key.isNotBlank() }.forEach { h ->
            sb.append("${h.key}: ${h.value}\n")
        }
        if (spec.body.isNotBlank()) {
            sb.append("\n").append(spec.body)
        }
        return TextOpResult.Success(sb.toString().trimEnd())
    }
}
