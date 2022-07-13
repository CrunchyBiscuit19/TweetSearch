package com.example.tweetsearch.ui.theme

import androidx.annotation.ColorRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tweetsearch.R

private val LightColorPalette = lightColors(
    primary = Teal700,
    primaryVariant = Teal900,
    secondary = Brown400,
    background = Gray300,
    surface = Teal700,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = White,
)

private val DarkColorPalette = darkColors(
    primary = Teal700,
    primaryVariant = Teal900,
    secondary = Brown400,
    background = BlueGray800,
    surface = Teal700,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
)

@Composable
fun TweetSearchTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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