package com.grzeluu.habittracker.component.habit.domain.model

import kotlinx.datetime.LocalDateTime

data class HabitNotification(
    val dateTime: LocalDateTime,
    val habit: Habit
)
