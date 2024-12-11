package com.grzeluu.habittracker.feature.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SettingsLine(
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)
        .height(1.dp)
        .background(MaterialTheme.colorScheme.outline))
}