package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.util.enums.CardColor


@Composable
fun ColorCircle(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    cardColor: CardColor,
    size: Dp = 42.dp,
    onClicked: (CardColor) -> Unit
) {
    val color = cardColor.mapToColor()
    val colors = listOf(color, color.copy(alpha = 0.5f))
    val borderColor = MaterialTheme.colorScheme.onSurface

    Box {
        Canvas(modifier = modifier.size(size)) {
            drawCircle(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(0f, 0f),
                    end = Offset(size.toPx(), size.toPx())
                ),
                radius = size.toPx() / 2,
                center = Offset(size.toPx() / 2, size.toPx() / 2)
            )
            if (isSelected) {
                drawCircle(
                    color = borderColor,
                    radius = size.toPx() / 2,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .clickable { onClicked(cardColor) })
    }
}