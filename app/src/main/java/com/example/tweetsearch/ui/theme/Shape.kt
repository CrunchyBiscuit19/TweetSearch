package com.example.tweetsearch.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp),
)

val buttonRoundCorners = RoundedCornerShape(20)
val imageRoundCorners = RoundedCornerShape(5)
val defaultTextModifier = Modifier
    .fillMaxWidth()
    .padding(8.dp)