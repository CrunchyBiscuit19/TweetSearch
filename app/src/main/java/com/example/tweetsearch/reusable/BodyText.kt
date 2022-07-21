package com.example.tweetsearch.reusable

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tweetsearch.R
import com.example.tweetsearch.ui.theme.defaultModifier

@Composable
fun BodyText (modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier,
    )
}

@Composable
fun HeaderBodyText (modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
    )
}

@Composable
fun ErrorBodyText (modifier: Modifier = Modifier, text: String) {
    Text(
        text,
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.error,
        modifier = modifier,
    )
}