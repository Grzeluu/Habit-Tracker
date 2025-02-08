package com.grzeluu.habittracker.component.habit.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.grzeluu.habittracker.component.habit.data.mapper.mapToDomain
import com.grzeluu.habittracker.component.habit.data.mapper.mapToEntity
import com.grzeluu.habittracker.component.habit.data.receiver.NotificationReceiver.Companion.createNotificationIntent
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.source.database.data.dao.NotificationDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao,
    @ApplicationContext private val context: Context
) : NotificationRepository {
    override fun getHabitsNotification(): Flow<List<HabitNotification>> {
        return notificationDao.getAllNotifications().map { it.map { notification -> notification.mapToDomain() } }
    }

    override suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotification) {
        notificationDao.addOrUpdateHabitNotification(habitNotification.mapToEntity())
    }

    override suspend fun deleteHabitNotificationByHabitId(notificationId: Long): HabitNotification? {
        val notification = notificationDao.getNotificationByHabitId(notificationId)
            .map { it?.mapToDomain() }
            .firstOrNull()
            ?: return null

        notificationDao.deleteHabitNotificationByHabitId(notificationId)
        return notification
    }
}