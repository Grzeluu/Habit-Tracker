package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import kotlinx.datetime.LocalDate

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: CardIcon,
    val color: CardColor,
    val description: String?,
    val periodicity: HabitPeriodicity,
    val notificationTime: HabitTime?,
    val effort: HabitDesiredEffort?,
    val history: List<HabitHistoryEntry> = emptyList()
) {
    fun isPerformedOnDate(date: LocalDate): Boolean {
        return history.any { it.date == date }
    }

    fun getHistoryEntryForDate(date: LocalDate): HabitHistoryEntry? {
        return history.find { it.date == date }
    }

}