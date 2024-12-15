package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlin.math.min

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: CardIcon,
    val color: CardColor,
    val description: String?,
    val desirableDays: List<Day>,
    val habitNotification: HabitNotification,
    val effort: HabitDesiredEffort,
    val additionDate: LocalDate,
    val history: List<HabitHistoryEntry> = emptyList(),
    val isArchive: Boolean = false,
) {
    val totalEffort: Float
        get() = history.map { it.currentEffort.toDouble() }.sumOf { it }.toFloat()

    fun getProgress(currentDate: LocalDate): Float {
        val historyEntry = history.find { it.date == currentDate } ?: return 0f
        return min(historyEntry.currentEffort / effort.desiredValue, 1f)
    }

    fun getCurrentStreak(currentDate: LocalDate): Int {
        val sortedHistory = history.sortedByDescending { it.date }
        val latestEntryDate = sortedHistory.firstOrNull()?.date ?: return 0

        var currentStreak = 0

        var lastDate = currentDate
        fun lastDateIsNotDesirable() = !desirableDays.contains(Day.get(lastDate.dayOfWeek.value))

        for (entry in sortedHistory) {
            while (lastDateIsNotDesirable()) {
                lastDate = lastDate.minus(1, DateTimeUnit.DAY)
            }
            when {
                entry.currentEffort > 0f && entry.date == lastDate -> {
                    currentStreak++
                    lastDate = lastDate.minus(1, DateTimeUnit.DAY)
                }

                else -> break

            }
        }
        return currentStreak
    }
}