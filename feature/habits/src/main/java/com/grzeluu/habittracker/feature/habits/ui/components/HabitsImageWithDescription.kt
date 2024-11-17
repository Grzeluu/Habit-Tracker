package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun HabitsImageWithDescription(
    modifier: Modifier = Modifier,
    painter: Painter,
    description: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.8f),
            painter = painter,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}