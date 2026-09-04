package com.xtech.xdevpocket.domain.utilities

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

sealed class JsonResult {
    data class Success(val output: String) : JsonResult()
    data class Error(val message: String) : JsonResult()
}

/**
 * Formatting, minifying and validating JSON using org.json (bundled with Android).
 * No external parser dependency needed, keeping the app fully offline and lightweight.
 */
object JsonFormatter {

    fun format(input: String, indent: Int = 2): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")
        return try {
            val trimmed = input.trim()
            val pretty = when {
                trimmed.startsWith("{") -> JSONObject(trimmed).toString(indent)
                trimmed.startsWith("[") -> JSONArray(trimmed).toString(indent)
                else -> throw JSONException("JSON must start with '{' or '['.")
            }
            JsonResult.Success(pretty)
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e, input))
        }
    }

    fun minify(input: String): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")
        return try {
            val trimmed = input.trim()
            val compact = when {
                trimmed.startsWith("{") -> JSONObject(trimmed).toString()
                trimmed.startsWith("[") -> JSONArray(trimmed).toString()
                else -> throw JSONException("JSON must start with '{' or '['.")
            }
            JsonResult.Success(compact)
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e, input))
        }
    }

    fun validate(input: String): JsonResult {
        if (input.isBlank()) return JsonResult.Error("Input is empty.")
        return try {
            val trimmed = input.trim()
            when {
                trimmed.startsWith("{") -> JSONObject(trimmed)
                trimmed.startsWith("[") -> JSONArray(trimmed)
                else -> throw JSONException("JSON must start with '{' or '['.")
            }
            JsonResult.Success("Valid JSON")
        } catch (e: JSONException) {
            JsonResult.Error(describeError(e, input))
        }
    }

    private fun describeError(e: JSONException, input: String): String {
        val msg = e.message ?: "Malformed JSON."
        return "Invalid JSON\n\n$msg"
    }
}
