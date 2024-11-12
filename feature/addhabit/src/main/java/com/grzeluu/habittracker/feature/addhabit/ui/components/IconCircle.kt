package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.util.enums.CardIcon


@Composable
fun IconCircle(
    modifier: Modifier = Modifier,
    icon: CardIcon,
    contentDescription: String? = null,
    color: androidx.compose.ui.graphics.Color,
    size: Dp = 42.dp,
    onClicked: (CardIcon) -> Unit
) {

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable { onClicked(icon) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon.mapToDrawableRes()),
            contentDescription = contentDescription,
            modifier = Modifier.size(size - 8.dp),
            tint = color
        )
    }
}