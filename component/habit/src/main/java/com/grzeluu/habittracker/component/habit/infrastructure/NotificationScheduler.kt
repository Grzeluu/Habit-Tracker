package com.grzeluu.habittracker.component.habit.infrastructure

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification

interface NotificationScheduler {
    fun initNotificationChannel()
    fun scheduleNotification(habitNotification: HabitNotification)
    fun cancelNotification(habitNotification: HabitNotification)
}