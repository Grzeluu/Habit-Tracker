package com.grzeluu.habittracker.feature.calendar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grzeluu.habittracker.common.ui.card.SimpleHabitCard
import com.grzeluu.habittracker.component.habit.domain.model.Habit

@Composable
fun HabitsListCard(habit: Habit, onCardClicked: () -> Unit) {
    SimpleHabitCard(
        modifier = Modifier.fillMaxWidth(),
        habitName = habit.name,
        habitDescription = habit.description,
        desiredEffortValue = habit.effort.desiredValue,
        effortUnit = habit.effort.effortUnit,
        habitIcon = habit.icon,
        habitColor = habit.color,
        isArchive = habit.isArchive,
        onCardClicked = onCardClicked
    )
}