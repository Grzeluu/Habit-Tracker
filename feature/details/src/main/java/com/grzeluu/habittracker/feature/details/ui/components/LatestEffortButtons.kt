package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate


@Composable
fun LatestEffortButtons(
    habit: Habit,
    lastDays: List<LocalDate>,
    onSelectDate: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        lastDays.forEachIndexed { index, date ->
            DailyProgressButton(
                modifier = Modifier.weight(1f),
                date = date,
                isActive = habit.shouldDailyButtonBeActive(date),
                color = habit.color.mapToColor(),
                progress = habit.getProgress(date),
                onClicked = { onSelectDate(date) },
            )
            if (index != Day.entries.lastIndex) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun Habit.shouldDailyButtonBeActive(date: LocalDate) =
    (desirableDays.contains(Day.get(date.dayOfWeek.value)) || history.any { it.date == date })
