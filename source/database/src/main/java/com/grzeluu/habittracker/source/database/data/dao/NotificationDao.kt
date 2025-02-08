package com.grzeluu.habittracker.source.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationEntity
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationWithHabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotificationEntity)

    @Transaction
    @Query("SELECT * FROM habits_notifications INNER JOIN habits ON habits.id = habits_notifications.habit_id")
    fun getAllNotifications(): Flow<List<HabitNotificationWithHabitEntity>>

    @Query("DELETE FROM habits_notifications WHERE habit_id = :habitId")
    suspend fun deleteHabitNotificationByHabitId(habitId: Long)

}