package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.color.CardColors.ORANGE
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun DailyProgressButton(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClicked: () -> Unit,
    progress: Float,
    color: Color,
    isActive: Boolean,
) {
    Button(
        onClick = onClicked,
        enabled = isActive,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Box(modifier.drawBehind {
            drawCircle(
                color = color.copy(0.35f),
                center = Offset(x = 0f, y = 0f),
                radius = sqrt(size.width.pow(2) + size.height.pow(2)) * progress
            )
        }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp),
                    text = date.dayOfMonth.toString(),
                    style = TextStyle.Default.copy(fontWeight = FontWeight.ExtraBold),
                )
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = Day.get(date.dayOfWeek.value).mapToUiText().asString(),
                    style = TextStyle.Default.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Preview
@Composable
fun DayToggleButtonPreview() {
    HabitTrackerTheme {
        DailyProgressButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 0.5f,
            color = ORANGE,
            isActive = true
        )
    }
}

@Preview
@Composable
fun DayToggleButtonPreviewDone() {
    HabitTrackerTheme {
        DailyProgressButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 1f,
            color = Color.Blue,
            isActive = true
        )
    }
}

@Preview
@Composable
fun DayToggleButtonPreviewNoProgress() {
    HabitTrackerTheme {
        DailyProgressButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 0f,
            color = ORANGE,
            isActive = false
        )
    }
}