package com.matttax.drivebetter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Violet,
    primaryVariant = Blue,
    secondary = Color.Gray,
    onSecondary = Color.White
)

private val LightColorPalette = lightColors(
    primary = Blue,
    onPrimary = Color.Black,
    secondary = Violet,
    onSecondary = Gray,
    surface = Color.White,
    onSurface = Color.Black,
    primaryVariant = Green,
    error = Red,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}