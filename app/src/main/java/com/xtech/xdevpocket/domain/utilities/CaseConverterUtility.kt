package com.xtech.xdevpocket.domain.utilities

/**
 * Converts text between common developer case styles.
 * Pure, offline, and tolerant of any input — punctuation and existing
 * separators are normalized before re-casing.
 */
object CaseConverterUtility {

    enum class CaseType(val label: String) {
        UPPER("UPPERCASE"),
        LOWER("lowercase"),
        TITLE("Title Case"),
        SENTENCE("Sentence case"),
        CAMEL("camelCase"),
        PASCAL("PascalCase"),
        SNAKE("snake_case"),
        KEBAB("kebab-case"),
        CONSTANT("CONSTANT_CASE"),
    }

    private fun words(input: String): List<String> {
        // Split on whitespace, underscores, hyphens, and camel/Pascal boundaries.
        val spaced = input
            .replace(Regex("([a-z0-9])([A-Z])"), "$1 $2")
            .replace(Regex("[_\\-]+"), " ")
        return spaced.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    }

    fun convert(input: String, type: CaseType): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")
        val w = words(input)
        if (w.isEmpty()) return TextOpResult.Error("Nothing to convert.")

        val output = when (type) {
            CaseType.UPPER -> input.uppercase()
            CaseType.LOWER -> input.lowercase()
            CaseType.TITLE -> w.joinToString(" ") { it.lowercase().replaceFirstChar(Char::uppercase) }
            CaseType.SENTENCE -> w.joinToString(" ") { it.lowercase() }
                .replaceFirstChar(Char::uppercase)
            CaseType.CAMEL -> w.mapIndexed { i, s ->
                if (i == 0) s.lowercase() else s.lowercase().replaceFirstChar(Char::uppercase)
            }.joinToString("")
            CaseType.PASCAL -> w.joinToString("") { it.lowercase().replaceFirstChar(Char::uppercase) }
            CaseType.SNAKE -> w.joinToString("_") { it.lowercase() }
            CaseType.KEBAB -> w.joinToString("-") { it.lowercase() }
            CaseType.CONSTANT -> w.joinToString("_") { it.uppercase() }
        }
        return TextOpResult.Success(output)
    }
}
