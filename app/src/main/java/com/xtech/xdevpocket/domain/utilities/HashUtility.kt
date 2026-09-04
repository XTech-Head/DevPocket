package com.xtech.xdevpocket.domain.utilities

import java.security.MessageDigest

enum class HashAlgorithm(val label: String, val digestName: String, val recommended: Boolean) {
    MD5("MD5", "MD5", recommended = false),
    SHA1("SHA-1", "SHA-1", recommended = false),
    SHA256("SHA-256", "SHA-256", recommended = true),
    SHA384("SHA-384", "SHA-384", recommended = true),
    SHA512("SHA-512", "SHA-512", recommended = true),
}

object HashUtility {

    fun hash(input: String, algorithm: HashAlgorithm): TextOpResult {
        if (input.isEmpty()) return TextOpResult.Error("Input is empty.")
        return try {
            val digest = MessageDigest.getInstance(algorithm.digestName)
            val bytes = digest.digest(input.toByteArray(Charsets.UTF_8))
            val hex = bytes.joinToString("") { "%02x".format(it) }
            TextOpResult.Success(hex)
        } catch (e: Exception) {
            TextOpResult.Error("Unable to generate hash.")
        }
    }
}
