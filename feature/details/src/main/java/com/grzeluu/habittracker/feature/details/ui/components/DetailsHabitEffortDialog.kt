package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.runtime.Composable
import com.grzeluu.habittracker.common.ui.dialog.HabitEffortDialog
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import kotlinx.datetime.LocalDate

@Composable
fun DetailsHabitEffortDialog(
    habit: Habit,
    selectedDate: LocalDate,
    onSetProgress: (Float) -> Unit,
    onDismiss: () -> Unit,
) {
    with(habit) {
        HabitEffortDialog(
            habitName = name,
            entryDate = selectedDate,
            isDialogVisible = true,
            currentEffort = history.find { it.date == selectedDate }?.currentEffort ?: 0f,
            habitIcon = icon,
            habitColor = color,
            habitDescription = description,
            effortUnit = effort.effortUnit,
            desiredEffortValue = effort.desiredValue,
            onDismissRequest = onDismiss,
            onSetProgress = onSetProgress
        )
    }
}
