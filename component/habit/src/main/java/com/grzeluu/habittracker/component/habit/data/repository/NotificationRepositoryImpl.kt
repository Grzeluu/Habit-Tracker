package com.grzeluu.habittracker.component.habit.data.repository

import com.grzeluu.habittracker.component.habit.data.mapper.mapToDomain
import com.grzeluu.habittracker.component.habit.data.mapper.mapToEntity
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.source.database.data.dao.NotificationDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override fun getHabitsNotification(): Flow<List<HabitNotification>> {
        return notificationDao.getAllNotifications().map { it.map { notification -> notification.mapToDomain() } }
    }

    override suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotification) {
        notificationDao.addOrUpdateHabitNotification(habitNotification.mapToEntity())
    }

    override suspend fun deleteNotificationsByHabitId(id: Long) {
        notificationDao.deleteHabitNotificationByHabitId(id)
    }
}