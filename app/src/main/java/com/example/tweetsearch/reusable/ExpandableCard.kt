package com.example.tweetsearch.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(
    modifier: Modifier,
    shape: Shape,
    backgroundColor: Color,
    elevation: Dp,
    folded: Boolean,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        elevation = elevation,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
        ) {
            header()
            if (!folded) {
                content()
            }
        }
    }
}