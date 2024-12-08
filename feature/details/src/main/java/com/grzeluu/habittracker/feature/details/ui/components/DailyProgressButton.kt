package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.background.FilledBackground
import com.grzeluu.habittracker.common.ui.color.CardColors.ORANGE
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

@Composable
fun DayToggleButton(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClicked: () -> Unit,
    progress: Float,
    color: Color,
) {
    Button(
        onClick = onClicked,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = color.copy(alpha = progress),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Box {
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
        DayToggleButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 0.5f,
            color = ORANGE
        )
    }
}

@Preview
@Composable
fun DayToggleButtonPreviewDone() {
    HabitTrackerTheme {
        DayToggleButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 1f,
            color = ORANGE
        )
    }
}

@Preview
@Composable
fun DayToggleButtonPreviewNoProgress() {
    HabitTrackerTheme {
        DayToggleButton(
            modifier = Modifier.width(52.dp),
            date = LocalDate(2024, 12, 12),
            onClicked = {},
            progress = 0f,
            color = ORANGE
        )
    }
}