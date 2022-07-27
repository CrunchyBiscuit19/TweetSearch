package com.example.tweetsearch.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweetsearch.enum.DarkModeValidOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: DataStore<Preferences>
) : ViewModel() {
    private val darkModeKey = stringPreferencesKey("darkMode")
    var uiState by mutableStateOf(UiState())

    init { refresh() }
    private fun refresh() {
        viewModelScope.launch {
            settingsDataStore.data.collectLatest {
                uiState.darkModeOption = when (it[darkModeKey]) {
                    DarkModeValidOptions.ON.name -> DarkModeValidOptions.ON
                    DarkModeValidOptions.OFF.name -> DarkModeValidOptions.OFF
                    DarkModeValidOptions.SYSTEM.name -> DarkModeValidOptions.SYSTEM
                    else -> DarkModeValidOptions.SYSTEM
                }
            }
        }
    }

    fun setDarkModeOption(darkModeOption: DarkModeValidOptions) {
        viewModelScope.launch {
            settingsDataStore.edit {
                it[darkModeKey] = darkModeOption.name
            }
        }
    }
}