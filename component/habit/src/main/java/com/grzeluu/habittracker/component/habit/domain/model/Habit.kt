package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

data class Habit(
    val id: Long = 0,
    val name: String,
    val icon: CardIcon,
    val color: CardColor,
    val description: String?,
    val desirableDays: List<Day>,
    val habitNotification: HabitNotification,
    val effort: HabitDesiredEffort,
    val history: List<HabitHistoryEntry> = emptyList(),
    val isArchive: Boolean = false
) {
    fun currentStreak(currentDate: LocalDate): Int {
        val sortedHistory = history.sortedByDescending { it.date }
        val latestEntryDate = sortedHistory.firstOrNull()?.date ?: return 0

        if (latestEntryDate != currentDate && latestEntryDate != currentDate.minus(1, DateTimeUnit.DAY)) return 0

        var currentStreak = 0
        var lastDate = latestEntryDate

        fun lastDateIsNotDesirable() = !desirableDays.contains(Day.get(lastDate.dayOfWeek.value))

        for (entry in sortedHistory) {
            when {
                entry.currentEffort > 0f && entry.date == lastDate -> {
                    currentStreak++
                }

                entry.date != currentDate -> {
                    break
                }
            }
            do {
                lastDate = lastDate.minus(1, DateTimeUnit.DAY)
            } while (lastDateIsNotDesirable())
        }
        return currentStreak
    }
}