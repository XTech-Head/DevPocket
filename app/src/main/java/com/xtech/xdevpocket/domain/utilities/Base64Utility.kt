package com.xtech.xdevpocket.domain.utilities

import android.util.Base64

sealed class TextOpResult {
    data class Success(val output: String) : TextOpResult()
    data class Error(val message: String) : TextOpResult()
}

object Base64Utility {

    fun encode(input: String): TextOpResult {
        if (input.isEmpty()) return TextOpResult.Error("Input is empty.")
        return try {
            val encoded = Base64.encodeToString(input.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            TextOpResult.Success(encoded)
        } catch (e: Exception) {
            TextOpResult.Error("Unable to encode input.")
        }
    }

    fun decode(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")

        val normalized = input.trim().replace("\n", "").replace("\r", "")

        return try {
            val decodedBytes = Base64.decode(normalized, Base64.DEFAULT)
            val decoded = String(decodedBytes, Charsets.UTF_8)

            // Verify the decoded bytes reproduce the original Base64.
            val check = Base64.encodeToString(decodedBytes, Base64.NO_WRAP)

            if (check != normalized) {
                TextOpResult.Error("Unable to decode Base64.\n\nCheck the input and try again.")
            } else {
                TextOpResult.Success(decoded)
            }
        } catch (_: Exception) {
            TextOpResult.Error("Unable to decode Base64.\n\nCheck the input and try again.")
        }
    }
}
