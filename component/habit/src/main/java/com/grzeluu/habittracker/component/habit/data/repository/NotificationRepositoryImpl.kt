package com.grzeluu.habittracker.component.habit.data.repository

import com.grzeluu.habittracker.component.habit.data.mapper.mapToEntity
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.source.database.data.dao.NotificationDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotification) {
        notificationDao.addOrUpdateHabitNotification(habitNotification.mapToEntity())
    }

    override suspend fun deleteNotificationsByHabitId(id: Long) {
        notificationDao.deleteHabitNotificationByHabitId(id)
    }
}