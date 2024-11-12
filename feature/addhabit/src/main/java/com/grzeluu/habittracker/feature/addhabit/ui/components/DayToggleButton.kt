package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.util.enums.Day

@Composable
fun DayToggleButton(
    day: Day,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onCheckedChange(!isChecked) },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp)),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = day.mapToUiText().asString(),
            color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}