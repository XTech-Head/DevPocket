package com.xtech.xdevpocket.domain.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimestampUtility {

    private fun formatter(): SimpleDateFormat {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf
    }

    fun secondsToReadable(seconds: Long): String = formatter().format(Date(seconds * 1000))

    fun millisToReadable(millis: Long): String = formatter().format(Date(millis))

    fun convertUnixToDate(input: String): TextOpResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return TextOpResult.Error("Input is empty.")
        val value = trimmed.toLongOrNull()
            ?: return TextOpResult.Error("Enter a valid numeric timestamp.")

        // Heuristic: 10 digits ~ seconds, 13 digits ~ millis
        return try {
            val readable = if (trimmed.length >= 13) millisToReadable(value) else secondsToReadable(value)
            TextOpResult.Success(readable)
        } catch (e: Exception) {
            TextOpResult.Error("Unable to convert timestamp.")
        }
    }

    fun convertDateToUnix(input: String): TextOpResult {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return TextOpResult.Error("Input is empty.")
        val patterns = listOf(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd",
            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy",
        )
        for (pattern in patterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                sdf.isLenient = false
                val date = sdf.parse(trimmed)
                if (date != null) {
                    return TextOpResult.Success((date.time / 1000).toString())
                }
            } catch (_: Exception) {
                // try next pattern
            }
        }
        return TextOpResult.Error(
            "Unable to parse date.\n\nTry a format like yyyy-MM-dd HH:mm:ss."
        )
    }

    fun nowUnixSeconds(): Long = System.currentTimeMillis() / 1000
}
