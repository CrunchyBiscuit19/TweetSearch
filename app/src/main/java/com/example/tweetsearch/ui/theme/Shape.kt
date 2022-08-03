package com.example.tweetsearch.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val DEFAULT_PADDING = 8.dp
val BUTTON_ROUND_CORNERS = RoundedCornerShape(20)
val IMAGE_ROUND_CORNERS = RoundedCornerShape(5)
val DEFAULT_TEXT_MODIFIER = Modifier
    .fillMaxWidth()
    .padding(DEFAULT_PADDING)

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
)