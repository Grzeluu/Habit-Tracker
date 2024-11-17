package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import kotlin.math.min

data class DailyHabitInfo(
    val id: Long = 0,
    val name: String,
    val icon: CardIcon,
    val color: CardColor,
    val description: String?,
    val effort: HabitDesiredEffort,
    val dailyHistoryEntry: HabitHistoryEntry?
) {
    val currentEffort: Float
        get() = dailyHistoryEntry?.currentEffort ?: 0f

    val effortProgress: Float
        get() = min(currentEffort / effort.desiredValue, 1f)

    val isDone: Boolean
        get() = effortProgress >= 1f
}