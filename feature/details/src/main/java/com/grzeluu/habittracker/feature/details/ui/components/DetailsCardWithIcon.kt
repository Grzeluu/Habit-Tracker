package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.padding.AppSizes

@Composable
fun DetailsCardWithIcon(
    modifier: Modifier = Modifier,
    iconPainter: Painter,
    iconColor: Color? = null,
    label: String,
    body: String
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(AppSizes.cardInnerPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = iconPainter,
                contentDescription = null,
                tint = iconColor ?: LocalContentColor.current,
            )
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}