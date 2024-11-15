package com.grzeluu.habittracker.source.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity
import com.grzeluu.habittracker.source.database.data.model.HabitWithHistoryDbModel
import com.grzeluu.habittracker.source.database.data.model.HabitWithOneDayHistoryEntryDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitHistoryEntry(habitEntry: HabitHistoryEntryEntity)

    @Transaction
    suspend fun insertHabitWithHistoryEntries(habitWithHistory: HabitWithHistoryDbModel) {
        val habitId = insertHabit(habitWithHistory.habit)
        habitWithHistory.historyEntries.forEach { historyEntry ->
            insertHabitHistoryEntry(historyEntry.copy(habitId = habitId))
        }
    }

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitWithHistoryEntriesByHabitId(habitId: Long): Flow<HabitWithHistoryDbModel?>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM habits 
        LEFT JOIN habit_history_entries ON habits.id = habit_history_entries.habit_id
        WHERE habits.desirable_days LIKE '%' || :day || '%'  AND ( habit_history_entries.date = :date || habit_history_entries.date IS NULL)
        """
    )
    fun getHabitsWithDailyEntryByDayAndDate(day: String, date: LocalDate): Flow<List<HabitWithOneDayHistoryEntryDbModel>>
}