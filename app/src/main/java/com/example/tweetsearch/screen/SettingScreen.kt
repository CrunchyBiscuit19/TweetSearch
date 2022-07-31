package com.example.tweetsearch.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.BodyText
import com.example.tweetsearch.component.generic.HeaderBodyText
import com.example.tweetsearch.data.settings.*
import com.example.tweetsearch.dataStore
import com.example.tweetsearch.ui.theme.defaultTextModifier

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(SettingsRepository(LocalContext.current.dataStore))
    )
    val settingsPreferences by settingsViewModel.settingsPreferencesFlow.collectAsState(
        SettingsPreferences()
    )
    //TODO Switch to collectAsStateLifeCycle if it's possible to fix "Module compiled with different version of Kotlin error"

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        DarkModeSetting(
            modifier,
            settingsPreferences.darkModeOption
        ) { option -> settingsViewModel.updateDarkMode(option) }
        Divider()
        BodyText(defaultTextModifier, stringResource(R.string.developer_credit))
    }
}

@Composable
fun DarkModeSetting(
    modifier: Modifier,
    chosenOption: DarkModeValidOptions,
    updateOption: (DarkModeValidOptions) -> Unit
) {
    var settingExpanded by rememberSaveable { mutableStateOf(false) }

    SettingsMenuLink(
        icon = {
            Icon(
                imageVector = Icons.Filled.DarkMode,
                contentDescription = stringResource(R.string.dark_mode_options_icon_description),
                tint = MaterialTheme.colors.onBackground
            )
        },
        title = {
            Text(
                stringResource(R.string.dark_mode_setting_name),
                color = MaterialTheme.colors.onBackground
            )
        },
        subtitle = { Text(chosenOption.name, color = MaterialTheme.colors.onBackground) },
        modifier = modifier.background(MaterialTheme.colors.background),
        onClick = {
            settingExpanded = true
        },
    )
    if (settingExpanded) {
        AlertDialog(
            backgroundColor = MaterialTheme.colors.background,
            onDismissRequest = { settingExpanded = false },
            title = { HeaderBodyText(modifier, stringResource(R.string.dark_mode_setting_name)) },
            text = {
                Column(modifier.selectableGroup()) {
                    for (darkModeValidOption in DarkModeValidOptions.values()) {
                        Row(
                            modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (chosenOption == darkModeValidOption),
                                    onClick = {
                                        updateOption(darkModeValidOption)
                                    },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (chosenOption == darkModeValidOption),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(MaterialTheme.colors.primary)
                            )
                            BodyText(defaultTextModifier, darkModeValidOption.name)
                        }
                    }
                }
            },
            buttons = {},
        )
    }

}