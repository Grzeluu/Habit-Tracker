package com.grzeluu.habittracker.component.habit.domain.model

import kotlinx.datetime.LocalTime

sealed class HabitNotification {
    data object Disabled : HabitNotification()
    data class Enabled(val time: LocalTime) : HabitNotification()
}