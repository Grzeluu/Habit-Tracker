package com.grzeluu.habittracker.component.habit.domain.repository

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getHabitsNotification(): Flow<List<HabitNotification>>

    suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotification)

    suspend fun deleteNotificationsByHabitId(id: Long)
}