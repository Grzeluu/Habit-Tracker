package com.grzeluu.habittracker.component.habit.domain.model

import kotlinx.datetime.LocalTime

sealed class HabitNotificationSetting {
    data object Disabled : HabitNotificationSetting()
    data class Enabled(val time: LocalTime) : HabitNotificationSetting()
}