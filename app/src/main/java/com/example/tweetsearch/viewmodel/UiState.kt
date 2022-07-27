package com.example.tweetsearch.viewmodel

import com.example.tweetsearch.enum.DarkModeValidOptions

data class UiState (
    var darkModeOption: DarkModeValidOptions = DarkModeValidOptions.SYSTEM
)