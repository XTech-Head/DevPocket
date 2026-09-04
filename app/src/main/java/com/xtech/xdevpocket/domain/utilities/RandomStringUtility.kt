package com.xtech.xdevpocket.domain.utilities

import java.security.SecureRandom

object RandomStringUtility {

    private const val UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private const val LOWER = "abcdefghijklmnopqrstuvwxyz"
    private const val DIGITS = "0123456789"
    private const val SYMBOLS = "!@#\$%^&*()-_=+[]{}"

    private val random = SecureRandom()

    data class Options(
        val length: Int = 16,
        val includeUpper: Boolean = true,
        val includeLower: Boolean = true,
        val includeDigits: Boolean = true,
        val includeSymbols: Boolean = false,
    )

    fun generate(options: Options): TextOpResult {
        val pool = buildString {
            if (options.includeUpper) append(UPPER)
            if (options.includeLower) append(LOWER)
            if (options.includeDigits) append(DIGITS)
            if (options.includeSymbols) append(SYMBOLS)
        }
        if (pool.isEmpty()) {
            return TextOpResult.Error("Select at least one character set.")
        }
        val length = options.length.coerceIn(1, 256)
        val result = buildString {
            repeat(length) { append(pool[random.nextInt(pool.length)]) }
        }
        return TextOpResult.Success(result)
    }
}
