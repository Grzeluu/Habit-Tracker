package com.grzeluu.habittracker.component.habit.domain.model

import kotlinx.datetime.LocalDate

data class HabitHistoryEntry (
    val date: LocalDate,
    val timeSpentInMinutes: Int,
    val notes: String?,
)