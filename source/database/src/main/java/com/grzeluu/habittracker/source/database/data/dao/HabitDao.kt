package com.grzeluu.habittracker.source.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitWithHistoryEntity
import com.grzeluu.habittracker.source.database.data.model.HabitWithOneDayHistoryEntryEntity
import kotlinx.datetime.LocalDate

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertHabit(habit: HabitEntity)

    @Update
    fun updateHabit(habit: HabitEntity)

    @Delete
    fun deleteHabit(habit: HabitEntity)

    @Transaction
    @Query("SELECT * FROM habits WHERE id = :habitId")
    fun getHabitWithHistoryEntriesByHabitId(habitId: Long): HabitWithHistoryEntity?

    @Transaction
    @Query(
        """
        SELECT * 
        FROM habits 
        LEFT JOIN habit_history_entries ON habits.id = habit_history_entries.habit_id
        WHERE habits.desirable_days LIKE '%' || :day || '%'  AND ( habit_history_entries.date = :date || habit_history_entries.date IS NULL)
        """
    )
    fun getHabitsWithDailyEntryByDayAndDate(day: String, date: LocalDate): List<HabitWithOneDayHistoryEntryEntity>
}