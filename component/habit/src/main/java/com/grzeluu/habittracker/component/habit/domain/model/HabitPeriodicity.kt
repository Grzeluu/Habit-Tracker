package com.grzeluu.habittracker.component.habit.domain.model

data class HabitPeriodicity(
    val desirableDays: List<Int>?,
    private val defaultDaysInWeek: Int
) {
    val daysInWeek: Int
        get() = desirableDays?.size ?: defaultDaysInWeek
}
