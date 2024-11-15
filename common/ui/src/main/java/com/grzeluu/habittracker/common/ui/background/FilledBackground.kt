package com.grzeluu.habittracker.common.ui.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun FilledBackground(modifier: Modifier = Modifier, color: Color, fill: Float) {

    Canvas(
        modifier = modifier
    ) {
        drawCircle(
            color = color,
            center = Offset(x = size.width * 0.1f, y = size.height * 0.1f),
            radius = size.maxDimension * fill
        )
    }
}