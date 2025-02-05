package com.grzeluu.habittracker.component.habit.domain.repository

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification

interface NotificationRepository {
    suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotification)
}