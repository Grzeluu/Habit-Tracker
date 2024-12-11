package com.grzeluu.habittracker.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SettingsRow(
    modifier: Modifier = Modifier,
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    trailingElement: (@Composable () -> Unit)? = null,
) {
    Row(modifier = modifier
        .clickable { onClick.invoke() }
        .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = icon, contentDescription = null)
        Text(
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            text = title,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        trailingElement?.invoke()
    }
}