package com.grzeluu.habittracker.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.grzeluu.habittracker.source.database.data.converters.Converters
import com.grzeluu.habittracker.source.database.data.dao.HabitDao
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity

@Database(
    entities = [
        HabitEntity::class,
        HabitHistoryEntryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HabitTrackerDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}