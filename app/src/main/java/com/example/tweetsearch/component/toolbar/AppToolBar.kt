package com.example.tweetsearch.component.toolbar

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.tweetsearch.R
import com.example.tweetsearch.screen.Screen
import kotlinx.coroutines.launch

@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.placeholder),
    scaffoldState: ScaffoldState,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title, modifier, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    if (scaffoldState.drawerState.isOpen) {
                        scaffoldState.drawerState.close()
                    } else {
                        scaffoldState.drawerState.open()
                    }
                }
            }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.drawer_icon_description)
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Setting.name) }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = stringResource(R.string.settings_icon_description),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    )
}