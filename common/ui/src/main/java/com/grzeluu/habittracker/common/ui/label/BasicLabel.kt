package com.grzeluu.habittracker.common.ui.label

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BasicLabel(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier
            .padding(start = 4.dp, bottom = 4.dp, end = 4.dp),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.labelLarge
    )

}