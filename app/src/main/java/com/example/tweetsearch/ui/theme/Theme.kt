package com.example.tweetsearch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tweetsearch.data.setting.*
import com.example.tweetsearch.dataStore

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
fun TweetSearchTheme(content: @Composable () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(SettingsRepository(LocalContext.current.dataStore))
    )
    val settingsPreferences by settingsViewModel.settingsPreferencesFlow.collectAsState(
        SettingsPreferences()
    )
    val darkMode = when (settingsPreferences.darkModeOption) {
        DarkModeValidOptions.On -> true
        DarkModeValidOptions.Off -> false
        DarkModeValidOptions.System -> isSystemInDarkTheme()
    }

    val colors = if (darkMode) {
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