package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.util.date.getCurrentDate
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

@Composable
fun DaysOfWeekRow(
    modifier: Modifier = Modifier,
    selectedDay: LocalDate,
    daysOfWeek: List<Pair<Day, LocalDate>>,
    onDayClicked: (LocalDate) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            DayOfWeekToggleButton(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                isChecked = selectedDay == day.second,
                day = day.first,
                date = day.second,
                onClicked = { onDayClicked(day.second) },
                isToday = day.second == getCurrentDate()
            )
            if (index != Day.entries.lastIndex) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}