package com.grzeluu.habittracker.component.habit.domain.repository

import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitRepository {
    suspend fun addHabit(habit: Habit)

    fun getDailyHabitInfos(day: Day, dateTime: LocalDate): Flow<List<DailyHabitInfo>>
}