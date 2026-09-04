package com.xtech.xdevpocket.domain.utilities

/**
 * Builds well-formed Conventional Commits (https://www.conventionalcommits.org)
 * messages from structured fields. Pure text formatting — no git binary,
 * no repo access, nothing leaves the device.
 */
object CommitMessageUtility {

    enum class CommitType(val prefix: String, val label: String) {
        FEAT("feat", "Feature"),
        FIX("fix", "Bug fix"),
        DOCS("docs", "Documentation"),
        STYLE("style", "Style (formatting)"),
        REFACTOR("refactor", "Refactor"),
        PERF("perf", "Performance"),
        TEST("test", "Tests"),
        BUILD("build", "Build system"),
        CI("ci", "CI config"),
        CHORE("chore", "Chore"),
        REVERT("revert", "Revert"),
    }

    data class Options(
        val type: CommitType = CommitType.FEAT,
        val scope: String = "",
        val subject: String = "",
        val body: String = "",
        val breakingChange: String = "",
        val issueRefs: String = "",
    )

    fun build(options: Options): TextOpResult {
        val subject = options.subject.trim()
        if (subject.isEmpty()) {
            return TextOpResult.Error("Subject is required.")
        }
        if (subject.length > 72) {
            return TextOpResult.Error("Subject should be 72 characters or fewer (currently ${subject.length}).")
        }

        val scope = options.scope.trim()
        val isBreaking = options.breakingChange.isNotBlank()

        val header = buildString {
            append(options.type.prefix)
            if (scope.isNotEmpty()) append("(").append(scope).append(")")
            if (isBreaking) append("!")
            append(": ").append(subject)
        }

        val output = buildString {
            appendLine(header)

            val body = options.body.trim()
            if (body.isNotEmpty()) {
                appendLine()
                appendLine(body)
            }

            if (isBreaking) {
                appendLine()
                appendLine("BREAKING CHANGE: ${options.breakingChange.trim()}")
            }

            val refs = options.issueRefs.trim()
            if (refs.isNotEmpty()) {
                appendLine()
                appendLine(refs)
            }
        }.trimEnd()

        return TextOpResult.Success(output)
    }
}
