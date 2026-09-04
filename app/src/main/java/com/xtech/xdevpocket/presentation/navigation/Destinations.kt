package com.xtech.xdevpocket.presentation.navigation

sealed class Destination(val route: String) {
    data object Splash : Destination("splash")
    data object Home : Destination("home")
    data object Favorites : Destination("favorites")
    data object History : Destination("history")
    data object Settings : Destination("settings")

    data object JsonTool : Destination("tool/json")
    data object Base64Tool : Destination("tool/base64")
    data object UrlTool : Destination("tool/url")
    data object HashTool : Destination("tool/hash")
    data object JwtTool : Destination("tool/jwt")
    data object UuidTool : Destination("tool/uuid")
    data object RegexTool : Destination("tool/regex")
    data object TimestampTool : Destination("tool/timestamp")
    data object CaseConverterTool : Destination("tool/case_converter")
    data object RandomStringTool : Destination("tool/random_string")
    data object ColorConverterTool : Destination("tool/color_converter")
    data object CronHelperTool : Destination("tool/cron_helper")
    data object XmlFormatterTool : Destination("tool/xml_formatter")
    data object SqlFormatterTool : Destination("tool/sql_formatter")
    data object HttpBuilderTool : Destination("tool/http_builder")
    data object GitignoreGeneratorTool : Destination("tool/gitignore_generator")
    data object CommitMessageBuilderTool : Destination("tool/commit_message_builder")
}

data class BottomNavItem(
    val destination: Destination,
    val label: String,
)

val bottomNavItems = listOf(
    BottomNavItem(Destination.Home, "Home"),
    BottomNavItem(Destination.Favorites, "Favorites"),
    BottomNavItem(Destination.History, "History"),
    BottomNavItem(Destination.Settings, "Settings"),
)
