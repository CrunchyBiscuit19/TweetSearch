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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.example.tweetsearch.R
import com.example.tweetsearch.enum.DarkModeValidOptions
import com.example.tweetsearch.reusable.BodyText
import com.example.tweetsearch.reusable.HeaderBodyText
import com.example.tweetsearch.ui.theme.defaultModifier
import com.example.tweetsearch.viewmodel.SettingsViewModel

@Composable
fun Setting(modifier: Modifier = Modifier, settingsViewModel: SettingsViewModel) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        DarkModeSetting(
            modifier,
            settingsViewModel
        )
        Divider()
        BodyText(defaultModifier, stringResource(R.string.developer_credit))
    }
}

@Composable
fun DarkModeSetting(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel,
) {
    var settingExpanded by rememberSaveable { mutableStateOf(false) }
    var chosenOption = settingsViewModel.uiState.darkModeOption
    var changeOption = settingsViewModel::setDarkModeOption

    SettingsMenuLink(
        icon = {
            Icon(
                imageVector = Icons.Filled.DarkMode,
                contentDescription = stringResource(R.string.dark_mode_options_icon_description)
            )
        },
        title = { Text(stringResource(R.string.dark_mode_setting_name)) },
        subtitle = { Text(chosenOption.name) },
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
                                        changeOption(darkModeValidOption)
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
                            BodyText(defaultModifier, darkModeValidOption.name)
                        }
                    }
                }
            },
            buttons = {},
        )
    }

}