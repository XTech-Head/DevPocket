package com.xtech.xdevpocket.domain.utilities

/**
 * Lightweight keyword-based SQL formatter. Not a full SQL parser — it uppercases
 * known keywords and breaks major clauses onto new lines, which covers the vast
 * majority of everyday formatting needs without pulling in a parser dependency.
 */
object SqlFormatterUtility {

    private val MAJOR_CLAUSES = listOf(
        "SELECT", "FROM", "WHERE", "GROUP BY", "ORDER BY", "HAVING",
        "LEFT JOIN", "RIGHT JOIN", "INNER JOIN", "OUTER JOIN", "JOIN",
        "LIMIT", "OFFSET", "UNION ALL", "UNION", "VALUES", "SET",
        "INSERT INTO", "UPDATE", "DELETE FROM",
    )

    private val KEYWORDS = MAJOR_CLAUSES + listOf(
        "AND", "OR", "NOT", "NULL", "IS", "IN", "LIKE", "BETWEEN", "AS",
        "ON", "DISTINCT", "COUNT", "SUM", "AVG", "MIN", "MAX", "ASC", "DESC",
        "CREATE TABLE", "ALTER TABLE", "DROP TABLE", "PRIMARY KEY", "FOREIGN KEY",
    )

    fun format(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")

        return try {
            var sql = input.trim().replace(Regex("\\s+"), " ")

            // Uppercase keywords (longest first so multi-word clauses match before their prefixes).
            KEYWORDS.sortedByDescending { it.length }.forEach { keyword ->
                sql = Regex("(?i)\\b${Regex.escape(keyword)}\\b").replace(sql, keyword)
            }

            // Break major clauses onto their own line.
            MAJOR_CLAUSES.sortedByDescending { it.length }.forEach { clause ->
                sql = sql.replace(" $clause ", "\n$clause ").replace(" $clause,", "\n$clause,")
                if (sql.startsWith("$clause ")) sql = sql // already at line start
            }

            // Indent items after SELECT / commas within the SELECT list lightly.
            sql = sql.replace(", ", ",\n  ")

            TextOpResult.Success(sql.lines().joinToString("\n") { it.trimEnd() }.trim())
        } catch (e: Exception) {
            TextOpResult.Error("Unable to format SQL.\n\nCheck the input and try again.")
        }
    }

    fun minify(input: String): TextOpResult {
        if (input.isBlank()) return TextOpResult.Error("Input is empty.")
        val compact = input.trim().replace(Regex("\\s+"), " ")
        return TextOpResult.Success(compact)
    }
}
