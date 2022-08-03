package com.example.tweetsearch.data.setting

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class SettingsPreferences(
    var darkModeOption: DarkModeValidOptions = DarkModeValidOptions.System
)

class SettingsRepository(private val settingsDataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val darkModeKey = stringPreferencesKey("dark_mode")
    }

    val settingsPreferencesFlow: Flow<SettingsPreferences> =
        settingsDataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUiPreferences(preferences)
        }

    suspend fun updateDarkMode(darkModeOption: DarkModeValidOptions) {
        settingsDataStore.edit { preferences ->
            preferences[PreferencesKeys.darkModeKey] = darkModeOption.name
        }
    }

    private fun mapUiPreferences(preferences: Preferences): SettingsPreferences {
        val darkModeOption =
            preferences[PreferencesKeys.darkModeKey] ?: DarkModeValidOptions.System.name
        return SettingsPreferences(DarkModeValidOptions.valueOf(darkModeOption))
    }

}