package com.xtech.xdevpocket.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DevPocketDarkColors = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = AppWhite,
    secondary = Aqua,
    onSecondary = BackgroundDark,
    tertiary = Purple,
    onTertiary = AppWhite,
    background = BackgroundDark,
    onBackground = AppWhite,
    surface = SurfaceDark,
    onSurface = AppWhite,
    surfaceVariant = ElevatedSurfaceDark,
    onSurfaceVariant = MutedWhite,
    outline = Color(0xFF1E293B),
    error = Danger,
    onError = AppWhite,
)

private val DevPocketLightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = Aqua,
    onSecondary = Color.White,
    tertiary = Purple,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = Color(0xFF0F172A),
    surface = SurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = ElevatedSurfaceLight,
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFE2E8F0),
    error = Danger,
    onError = Color.White,
)

enum class ThemeMode { DARK, LIGHT, SYSTEM }

@Composable
fun XDevPocketTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit,
) {
    val useDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDark) DevPocketDarkColors else DevPocketLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
