package com.grzeluu.habittracker.source.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity

@Dao
interface HabitEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitEntry(habitEntry: HabitHistoryEntryEntity)
}