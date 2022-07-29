package com.example.tweetsearch.component.generic

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier,
    )
}

@Composable
fun HeaderBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
    )
}

@Composable
fun ErrorBodyText(modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.error,
        modifier = modifier,
    )
}

@Composable
fun OptionsText(modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier,
    )
}