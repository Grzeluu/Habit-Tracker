package com.grzeluu.habittracker.feature.calendar.ui.model

import com.grzeluu.habittracker.component.habit.domain.model.Habit

data class HabitsListDataState(
    val activeHabits: List<Habit>,
    val archivedHabits: List<Habit>,
)