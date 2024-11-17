package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

@Composable
fun DayOfWeekToggleButton(
    modifier: Modifier = Modifier,
    day: Day,
    date: LocalDate,
    isToday: Boolean,
    isChecked: Boolean,
    onClicked: () -> Unit,

    ) {
    Button(
        onClick = onClicked,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = when {
                isChecked -> MaterialTheme.colorScheme.tertiaryContainer
                isToday -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isChecked) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surface)
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = date.dayOfMonth.toString(),
                style = TextStyle.Default.copy(fontWeight = FontWeight.ExtraBold),
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = day.mapToUiText().asString(),
                style = TextStyle.Default.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}