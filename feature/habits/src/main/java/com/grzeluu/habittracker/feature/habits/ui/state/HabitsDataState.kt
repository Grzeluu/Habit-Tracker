package com.grzeluu.habittracker.feature.habits.ui.state

import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

data class HabitsDataState(
    val daysOfWeek: List<Pair<Day, LocalDate>>,
    val selectedDay: LocalDate,
    val areHabitsAdded: Boolean,
)

