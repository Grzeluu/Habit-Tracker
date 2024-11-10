package com.grzeluu.habittracker.component.habit.domain.model

import kotlinx.datetime.LocalDate

data class HabitHistoryEntry (
    val id: Long = 0,
    val date: LocalDate,
    val currentEffort: Float,
    val notes: String?,
)