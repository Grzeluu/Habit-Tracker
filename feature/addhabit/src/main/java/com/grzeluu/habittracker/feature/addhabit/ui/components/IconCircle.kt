package com.grzeluu.habittracker.feature.addhabit.ui.components

import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon


@Composable
fun IconCircle(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    icon: CardIcon,
    contentDescription: String? = null,
    selectedColor: CardColor,
    size: Dp = 42.dp,
    onClicked: (CardIcon) -> Unit
) {
    val frameColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = frameColor, radius = size.toPx() / 2, style = Stroke(width = 2.dp.toPx())
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .clickable { onClicked(icon) },
            contentAlignment = Alignment.Center){
            Icon(
                painter = painterResource(icon.mapToDrawableRes()),
                contentDescription = contentDescription,
                modifier = Modifier.size(size - 8.dp),
                tint = selectedColor.mapToColor()
            )
        }
    }
}