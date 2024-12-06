package com.grzeluu.habittracker.component.habit.domain.repository

import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitRepository {
    fun getHabit(habitId: Long): Flow<Habit?>

    fun getDailyHabitInfos(day: Day, dateTime: LocalDate): Flow<List<DailyHabitInfo>>

    fun getHabitsCount(): Flow<Int>

    suspend fun addOrUpdateHabit(habit: Habit)

    suspend fun addHabitHistoryEntry(habitId: Long, habitHistoryEntry: HabitHistoryEntry)

    suspend fun deleteHabit(habit: Habit)

    suspend fun markHabitAsArchived(habitId: Long)
}