package com.xtech.xdevpocket.domain.utilities

data class RegexMatchResult(
    val fullMatch: String,
    val range: IntRange,
    val groups: List<String>,
)

sealed class RegexTestResult {
    data class Success(val matches: List<RegexMatchResult>) : RegexTestResult()
    data class Error(val message: String) : RegexTestResult()
}

object RegexUtility {

    fun test(pattern: String, input: String): RegexTestResult {
        if (pattern.isEmpty()) return RegexTestResult.Error("Enter a regular expression.")
        return try {
            val regex = Regex(pattern)
            val matches = regex.findAll(input).map { match ->
                RegexMatchResult(
                    fullMatch = match.value,
                    range = match.range,
                    groups = match.groupValues.drop(1),
                )
            }.toList()
            RegexTestResult.Success(matches)
        } catch (e: Exception) {
            RegexTestResult.Error("Invalid regular expression.\n\nCheck the pattern and try again.")
        }
    }

    val examples = listOf(
        "^\\d+$" to "Digits only",
        "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}" to "Email address",
        "https?://[^\\s]+" to "URL",
        "^[A-Z][a-z]*$" to "Capitalized word",
    )
}
