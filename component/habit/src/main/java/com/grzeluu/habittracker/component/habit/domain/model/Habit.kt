package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: CardIcon,
    val color: CardColor,
    val description: String?,
    val desirableDays: List<Day>?,
    val habitNotification: HabitNotification,
    val effort: HabitDesiredEffort,
    val history: List<HabitHistoryEntry> = emptyList(),
    val isArchive: Boolean = false
)