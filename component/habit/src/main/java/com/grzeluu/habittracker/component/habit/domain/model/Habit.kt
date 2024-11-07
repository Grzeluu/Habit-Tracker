package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.base.domain.enums.HabitColor
import com.grzeluu.habittracker.base.domain.enums.HabitIcon

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: HabitIcon,
    val color: HabitColor,
    val description: String?,
    val periodicity: HabitPeriodicity,
    val time: HabitPeriod?,
    val history: List<HabitHistoryEntry> = emptyList()
)