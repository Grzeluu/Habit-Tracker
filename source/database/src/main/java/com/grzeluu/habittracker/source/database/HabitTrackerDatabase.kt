package com.grzeluu.habittracker.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grzeluu.habittracker.source.database.data.converters.LocalDateConverter
import com.grzeluu.habittracker.source.database.data.converters.LocalDateTimeConverter
import com.grzeluu.habittracker.source.database.data.converters.LocalTimeConverter
import com.grzeluu.habittracker.source.database.data.converters.StringListConverter
import com.grzeluu.habittracker.source.database.data.dao.HabitDao
import com.grzeluu.habittracker.source.database.data.dao.NotificationDao
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitHistoryEntryEntity::class,
        HabitNotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    StringListConverter::class,
    LocalDateConverter::class,
    LocalTimeConverter::class,
    LocalDateTimeConverter::class
)
abstract class HabitTrackerDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun notificationDao(): NotificationDao
}