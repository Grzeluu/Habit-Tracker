package com.grzeluu.habittracker.source.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdateHabitNotification(habitNotification: HabitNotificationEntity)

    @Query("SELECT * FROM habits_notifications")
    fun getAllNotifications(): Flow<List<HabitNotificationEntity>>

    @Query("DELETE FROM habits_notifications WHERE habit_id = :habitId")
    suspend fun deleteHabitNotificationByHabitId(habitId: Long)

}