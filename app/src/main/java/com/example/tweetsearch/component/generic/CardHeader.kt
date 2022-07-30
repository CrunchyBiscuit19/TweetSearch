package com.example.tweetsearch.component.generic

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun CardHeader(modifier: Modifier = Modifier, title: String, arrowRotationAngle: Float) {
    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderBodyText(Modifier, title)
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = modifier.rotate(arrowRotationAngle),
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "Expand / Fold Card Icon",
            tint = MaterialTheme.colors.onBackground,
        )
    }
}