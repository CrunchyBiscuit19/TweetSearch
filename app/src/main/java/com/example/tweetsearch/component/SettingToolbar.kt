package com.example.tweetsearch.component

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.tweetsearch.R

@Composable
fun SettingToolbar(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.placeholder),
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.arrow_back_description)
                )
            }
        },
    )
}