package com.grzeluu.habittracker.feature.habits.ui.state

import kotlinx.datetime.LocalDate

data class HabitsDataState(
    val daysOfWeek: List<LocalDate>,
    val selectedDay: LocalDate,
    val areHabitsAdded: Boolean,
)

