package com.xtech.xdevpocket.domain.utilities

sealed class CronResult {
    data class Success(val description: String) : CronResult()
    data class Error(val message: String) : CronResult()
}

/**
 * Translates a standard 5-field cron expression (minute hour day-of-month month day-of-week)
 * into a plain-English description. This is a readable-summary helper, not a scheduler —
 * it never executes anything.
 */
object CronUtility {

    private val MONTH_NAMES = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    private val DOW_NAMES = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    fun describe(expression: String): CronResult {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return CronResult.Error("Input is empty.")

        val fields = trimmed.split(Regex("\\s+"))
        if (fields.size != 5) {
            return CronResult.Error(
                "Invalid cron expression.\n\nExpected 5 fields: minute hour day-of-month month day-of-week."
            )
        }

        return try {
            val (minute, hour, dom, month, dow) = fields
            val parts = mutableListOf<String>()

            parts += describeTime(minute, hour)
            parts += describeDayOfMonth(dom)
            parts += describeMonth(month)
            parts += describeDayOfWeek(dow)

            CronResult.Success(parts.filter { it.isNotBlank() }.joinToString(", "))
        } catch (e: Exception) {
            CronResult.Error("Invalid cron expression.\n\nCheck the field values and try again.")
        }
    }

    private fun describeTime(minute: String, hour: String): String {
        return when {
            minute == "*" && hour == "*" -> "Every minute"
            minute.startsWith("*/") && hour == "*" -> "Every ${minute.removePrefix("*/")} minutes"
            hour.startsWith("*/") && minute == "0" -> "Every ${hour.removePrefix("*/")} hours"
            minute.toIntOrNull() != null && hour.toIntOrNull() != null -> {
                val h = hour.toInt().coerceIn(0, 23)
                val m = minute.toInt().coerceIn(0, 59)
                "At %02d:%02d".format(h, m)
            }
            else -> "At minute $minute, hour $hour"
        }
    }

    private fun describeDayOfMonth(dom: String): String = when (dom) {
        "*" -> ""
        else -> "on day-of-month $dom"
    }

    private fun describeMonth(month: String): String {
        if (month == "*") return ""
        val index = month.toIntOrNull()
        return if (index != null && index in 1..12) "in ${MONTH_NAMES[index - 1]}" else "in month $month"
    }

    private fun describeDayOfWeek(dow: String): String {
        if (dow == "*") return ""
        val index = dow.toIntOrNull()
        return if (index != null && index in 0..6) "on ${DOW_NAMES[index]}" else "on day-of-week $dow"
    }
}
