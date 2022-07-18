package com.example.tweetsearch.reusable

import androidx.compose.foundation.border
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tweetsearch.R
import kotlinx.coroutines.launch

@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.placeholder),
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
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
                Icon(Icons.Filled.Menu, contentDescription = "Drawer menu")
            }
        },
        actions = {
            IconButton(onClick = { /* TODO Open Settings menu */ }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings menu",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
    )
}