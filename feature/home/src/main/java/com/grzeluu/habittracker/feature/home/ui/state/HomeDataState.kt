package com.grzeluu.habittracker.feature.home.ui.state

import kotlinx.datetime.LocalDate

data class HomeDataState(
    val daysOfWeek: List<LocalDate>,
    val selectedDay: LocalDate,
    val areHabitsAdded: Boolean,
)

