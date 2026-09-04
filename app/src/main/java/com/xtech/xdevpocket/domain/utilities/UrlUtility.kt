package com.xtech.xdevpocket.domain.utilities

import java.net.URLDecoder
import java.net.URLEncoder

object UrlUtility {

    fun encode(input: String): TextOpResult {
        if (input.isEmpty()) return TextOpResult.Error("Input is empty.")
        return try {
            TextOpResult.Success(URLEncoder.encode(input, "UTF-8"))
        } catch (e: Exception) {
            TextOpResult.Error("Unable to URL encode input.")
        }
    }

    fun decode(input: String): TextOpResult {
        if (input.isEmpty()) return TextOpResult.Error("Input is empty.")
        return try {
            TextOpResult.Success(URLDecoder.decode(input, "UTF-8"))
        } catch (e: Exception) {
            TextOpResult.Error("Unable to URL decode input.\n\nCheck the input and try again.")
        }
    }
}
