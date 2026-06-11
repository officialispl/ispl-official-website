package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldBright,
    secondary = GoldAmber,
    tertiary = NavyLightBlue,
    background = NavyDeepDusk,
    surface = NavyCardBlue,
    onPrimary = NavyDeepDusk,
    onSecondary = NavyDeepDusk,
    onBackground = SlateTextLight,
    onSurface = SlateTextLight,
    error = CrimsonStatus
)

private val LightColorScheme = darkColorScheme(
    primary = GoldBright,
    secondary = GoldAmber,
    tertiary = NavyLightBlue,
    background = NavyDeepDusk,
    surface = NavyCardBlue,
    onPrimary = NavyDeepDusk,
    onSecondary = NavyDeepDusk,
    onBackground = SlateTextLight,
    onSurface = SlateTextLight,
    error = CrimsonStatus
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic colors to preserve our brand Navy and Gold Identity
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
