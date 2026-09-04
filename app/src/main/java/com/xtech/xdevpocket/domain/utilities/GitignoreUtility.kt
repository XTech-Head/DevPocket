package com.xtech.xdevpocket.domain.utilities

/**
 * Generates .gitignore file content from a set of built-in, offline templates.
 *
 * Consistent with the rest of the app, this ships zero network calls — every
 * template is bundled in-app (no gitignore.io / GitHub API lookup), so it works
 * in airplane mode like everything else.
 */
object GitignoreUtility {

    data class Template(
        val id: String,
        val label: String,
        val content: String,
    )

    val templates: List<Template> = listOf(
        Template(
            id = "android",
            label = "Android",
            content = """
                *.apk
                *.aab
                *.ap_
                *.aar
                *.dex
                *.class
                bin/
                gen/
                out/
                release/
                .gradle/
                build/
                captures/
                local.properties
                .cxx/
                *.keystore
                *.jks
            """.trimIndent(),
        ),
        Template(
            id = "kotlin_java_gradle",
            label = "Kotlin / Java / Gradle",
            content = """
                *.class
                *.log
                *.jar
                !gradle-wrapper.jar
                hs_err_pid*
                .gradle/
                build/
                !gradle/wrapper/gradle-wrapper.jar
                .kotlin/
            """.trimIndent(),
        ),
        Template(
            id = "intellij",
            label = "IntelliJ IDEA / Android Studio",
            content = """
                .idea/
                *.iml
                *.ipr
                *.iws
                out/
                .idea_modules/
                caches/
            """.trimIndent(),
        ),
        Template(
            id = "vscode",
            label = "Visual Studio Code",
            content = """
                .vscode/*
                !.vscode/settings.json
                !.vscode/tasks.json
                !.vscode/launch.json
                !.vscode/extensions.json
                *.code-workspace
            """.trimIndent(),
        ),
        Template(
            id = "macos",
            label = "macOS",
            content = """
                .DS_Store
                .AppleDouble
                .LSOverride
                ._*
                .Spotlight-V100
                .Trashes
            """.trimIndent(),
        ),
        Template(
            id = "windows",
            label = "Windows",
            content = """
                Thumbs.db
                ehthumbs.db
                Desktop.ini
                ${'$'}RECYCLE.BIN/
                *.lnk
            """.trimIndent(),
        ),
        Template(
            id = "linux",
            label = "Linux",
            content = """
                *~
                .fuse_hidden*
                .directory
                .Trash-*
                .nfs*
            """.trimIndent(),
        ),
        Template(
            id = "node",
            label = "Node",
            content = """
                node_modules/
                npm-debug.log*
                yarn-debug.log*
                yarn-error.log*
                .pnpm-debug.log*
                dist/
                .env
                .env.local
            """.trimIndent(),
        ),
        Template(
            id = "python",
            label = "Python",
            content = """
                __pycache__/
                *.py[cod]
                *${'$'}py.class
                .Python
                venv/
                .venv/
                *.egg-info/
                .pytest_cache/
                .mypy_cache/
            """.trimIndent(),
        ),
        Template(
            id = "env_secrets",
            label = "Env & Secrets",
            content = """
                .env
                .env.*
                !.env.example
                *.pem
                *.key
                secrets.*
            """.trimIndent(),
        ),
    )

    fun byId(id: String): Template? = templates.find { it.id == id }

    /**
     * Merges the selected templates into a single .gitignore, de-duplicating
     * identical lines across sections while keeping each section's own
     * comment header for readability.
     */
    fun generate(selectedIds: List<String>): TextOpResult {
        if (selectedIds.isEmpty()) {
            return TextOpResult.Error("Select at least one template.")
        }
        val seenLines = LinkedHashSet<String>()
        val output = buildString {
            selectedIds.forEach { id ->
                val template = byId(id) ?: return@forEach
                val newLines = template.content.lines().filter { it.isNotBlank() && seenLines.add(it) }
                if (newLines.isNotEmpty()) {
                    appendLine("### ${template.label} ###")
                    newLines.forEach { appendLine(it) }
                    appendLine()
                }
            }
        }.trimEnd().plus("\n")

        return TextOpResult.Success(output)
    }
}
