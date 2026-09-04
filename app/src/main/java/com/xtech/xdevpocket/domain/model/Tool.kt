package com.xtech.xdevpocket.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector

enum class ToolCategory(val label: String) {
    JSON("JSON"),
    ENCODING("Encoding"),
    SECURITY("Security"),
    GENERATORS("Generators"),
    TEXT("Text"),
    TIME("Time"),
    UTILITIES("Utilities"),
    GIT("Git & GitHub"),
}

data class Tool(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: ToolCategory,
    val icon: ImageVector,
    val route: String,
)

object Tools {
    val JSON_FORMATTER = Tool(
        id = "json_formatter",
        title = "JSON Formatter",
        subtitle = "Format, minify & validate",
        category = ToolCategory.JSON,
        icon = Icons.Filled.DataObject,
        route = "tool/json",
    )
    val BASE64 = Tool(
        id = "base64",
        title = "Base64",
        subtitle = "Encode & decode",
        category = ToolCategory.ENCODING,
        icon = Icons.Filled.Code,
        route = "tool/base64",
    )
    val URL_ENCODER = Tool(
        id = "url_encoder",
        title = "URL Encoder",
        subtitle = "Encode & decode URLs",
        category = ToolCategory.ENCODING,
        icon = Icons.Filled.Link,
        route = "tool/url",
    )
    val HASH_GENERATOR = Tool(
        id = "hash_generator",
        title = "Hash Generator",
        subtitle = "MD5, SHA-1, SHA-256+",
        category = ToolCategory.SECURITY,
        icon = Icons.Filled.Fingerprint,
        route = "tool/hash",
    )
    val JWT_DECODER = Tool(
        id = "jwt_decoder",
        title = "JWT Decoder",
        subtitle = "Decode header & payload",
        category = ToolCategory.SECURITY,
        icon = Icons.Filled.VerifiedUser,
        route = "tool/jwt",
    )
    val UUID_GENERATOR = Tool(
        id = "uuid_generator",
        title = "UUID Generator",
        subtitle = "Generate UUID v4",
        category = ToolCategory.GENERATORS,
        icon = Icons.Filled.Tag,
        route = "tool/uuid",
    )
    val REGEX_TESTER = Tool(
        id = "regex_tester",
        title = "Regex Tester",
        subtitle = "Test patterns live",
        category = ToolCategory.TEXT,
        icon = Icons.Filled.Pattern,
        route = "tool/regex",
    )
    val TIMESTAMP = Tool(
        id = "timestamp",
        title = "Timestamp",
        subtitle = "Unix & date/time",
        category = ToolCategory.TIME,
        icon = Icons.Filled.Schedule,
        route = "tool/timestamp",
    )

    val CASE_CONVERTER = Tool(
        id = "case_converter",
        title = "Case Converter",
        subtitle = "camelCase, snake_case & more",
        category = ToolCategory.TEXT,
        icon = Icons.Filled.Pattern,
        route = "tool/case_converter",
    )
    val RANDOM_STRING = Tool(
        id = "random_string",
        title = "Random String",
        subtitle = "Generate secure random text",
        category = ToolCategory.GENERATORS,
        icon = Icons.Filled.Tag,
        route = "tool/random_string",
    )
    val COLOR_CONVERTER = Tool(
        id = "color_converter",
        title = "Color Converter",
        subtitle = "Hex, RGB & HSL",
        category = ToolCategory.UTILITIES,
        icon = Icons.Filled.DataObject,
        route = "tool/color_converter",
    )
    val CRON_HELPER = Tool(
        id = "cron_helper",
        title = "Cron Helper",
        subtitle = "Plain-English cron descriptions",
        category = ToolCategory.UTILITIES,
        icon = Icons.Filled.Schedule,
        route = "tool/cron_helper",
    )
    val XML_FORMATTER = Tool(
        id = "xml_formatter",
        title = "XML Formatter",
        subtitle = "Format, minify & validate",
        category = ToolCategory.TEXT,
        icon = Icons.Filled.DataObject,
        route = "tool/xml_formatter",
    )
    val SQL_FORMATTER = Tool(
        id = "sql_formatter",
        title = "SQL Formatter",
        subtitle = "Readable query formatting",
        category = ToolCategory.TEXT,
        icon = Icons.Filled.Code,
        route = "tool/sql_formatter",
    )
    val HTTP_BUILDER = Tool(
        id = "http_builder",
        title = "HTTP Request Builder",
        subtitle = "Build curl & raw requests",
        category = ToolCategory.UTILITIES,
        icon = Icons.Filled.Link,
        route = "tool/http_builder",
    )

    val GITIGNORE_GENERATOR = Tool(
        id = "gitignore_generator",
        title = ".gitignore Generator",
        subtitle = "Android, Kotlin, IDEs & more",
        category = ToolCategory.GIT,
        icon = Icons.Filled.Description,
        route = "tool/gitignore_generator",
    )
    val COMMIT_MESSAGE_BUILDER = Tool(
        id = "commit_message_builder",
        title = "Commit Message Builder",
        subtitle = "Conventional Commits format",
        category = ToolCategory.GIT,
        icon = Icons.Filled.Edit,
        route = "tool/commit_message_builder",
    )

    val all: List<Tool> = listOf(
        JSON_FORMATTER, BASE64, URL_ENCODER, HASH_GENERATOR,
        JWT_DECODER, UUID_GENERATOR, REGEX_TESTER, TIMESTAMP,
        CASE_CONVERTER, RANDOM_STRING, COLOR_CONVERTER, CRON_HELPER,
        XML_FORMATTER, SQL_FORMATTER, HTTP_BUILDER,
        GITIGNORE_GENERATOR, COMMIT_MESSAGE_BUILDER,
    )

    val quick: List<Tool> = listOf(JSON_FORMATTER, BASE64, HASH_GENERATOR, UUID_GENERATOR)

    fun byId(id: String): Tool? = all.find { it.id == id }

    fun search(query: String): List<Tool> {
        if (query.isBlank()) return all
        val q = query.trim().lowercase()
        return all.filter {
            it.title.lowercase().contains(q) ||
                it.subtitle.lowercase().contains(q) ||
                it.category.label.lowercase().contains(q)
        }
    }
}
