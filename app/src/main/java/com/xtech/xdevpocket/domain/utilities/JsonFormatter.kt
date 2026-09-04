package com.xtech.xdevpocket.domain.utilities

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

sealed class JsonResult {
    data class Success(val output: String) : JsonResult()
    data class Error(val message: String) : JsonResult()
}

/**
 * Formatting, minifying and validating JSON using org.json.
 * Uses JSONTokener to reject malformed JSON instead of the lenient parser.
 */
object JsonFormatter {

    fun format(input: String, indent: Int = 2): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")

        return try {
            val parsed = parseStrict(input)

            when (parsed) {
                is JSONObject -> JsonResult.Success(parsed.toString(indent))
                is JSONArray -> JsonResult.Success(parsed.toString(indent))
                else -> JsonResult.Error("Invalid JSON")
            }
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e))
        }
    }

    fun minify(input: String): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")

        return try {
            val parsed = parseStrict(input)

            when (parsed) {
                is JSONObject -> JsonResult.Success(parsed.toString())
                is JSONArray -> JsonResult.Success(parsed.toString())
                else -> JsonResult.Error("Invalid JSON")
            }
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e))
        }
    }

    fun validate(input: String): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")

        return try {
            parseStrict(input)
            JsonResult.Success("Valid JSON")
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e))
        }
    }

    // In JsonFormatter.kt
    @Throws(JSONException::class)
    private fun parseStrict(input: String): Any {
        val trimmed = input.trim()
        val tokener = JSONTokener(trimmed)
        val value = tokener.nextValue()

        // Check for trailing characters
        if (tokener.more()) {
            val next = tokener.nextClean()
            if (next != '\u0000') {
                throw JSONException("Extra characters after JSON end.")
            }
        }

        if (value !is JSONObject && value !is JSONArray) {
            throw JSONException("JSON must start with '{' or '['.")
        }

        return value
    }


    private fun describeError(e: JSONException): String {
        val msg = e.message ?: "Malformed JSON."
        return "Invalid JSON\n\n$msg"
    }
}