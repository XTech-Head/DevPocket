package com.xtech.xdevpocket.domain.utilities

import android.util.Base64
import org.json.JSONObject

data class JwtDecoded(
    val headerJson: String,
    val payloadJson: String,
    val algorithm: String?,
    val tokenType: String?,
    val issuedAt: String?,
    val expiresAt: String?,
)

sealed class JwtResult {
    data class Success(val decoded: JwtDecoded) : JwtResult()
    data class Error(val message: String) : JwtResult()
}

/**
 * Decodes a JWT locally. Does NOT verify the signature — this is a decode-only tool.
 * Nothing here ever leaves the device.
 */
object JwtUtility {

    fun decode(token: String): JwtResult {
        val trimmed = token.trim()
        if (trimmed.isEmpty()) return JwtResult.Error("Input is empty.")

        val parts = trimmed.split(".")
        if (parts.size != 3) {
            return JwtResult.Error("Invalid JWT format.\n\nExpected a token containing three sections.")
        }

        return try {
            val headerJson = decodeSegment(parts[0])
            val payloadJson = decodeSegment(parts[1])

            val headerObj = JSONObject(headerJson)
            val payloadObj = JSONObject(payloadJson)

            val alg = headerObj.optString("alg", null)
            val typ = headerObj.optString("typ", null)
            val iat = if (payloadObj.has("iat")) formatEpoch(payloadObj.optLong("iat")) else null
            val exp = if (payloadObj.has("exp")) formatEpoch(payloadObj.optLong("exp")) else null

            JwtResult.Success(
                JwtDecoded(
                    headerJson = headerObj.toString(2),
                    payloadJson = payloadObj.toString(2),
                    algorithm = alg,
                    tokenType = typ,
                    issuedAt = iat,
                    expiresAt = exp,
                )
            )
        } catch (e: Exception) {
            JwtResult.Error("Invalid JWT format.\n\nUnable to decode header or payload.")
        }
    }

    private fun decodeSegment(segment: String): String {
        val bytes = Base64.decode(segment, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        return String(bytes, Charsets.UTF_8)
    }

    private fun formatEpoch(epochSeconds: Long): String {
        return TimestampUtility.secondsToReadable(epochSeconds)
    }
}
