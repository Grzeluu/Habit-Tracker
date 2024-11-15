package com.grzeluu.habittracker.component.habit.data.repository

import com.grzeluu.habittracker.component.habit.data.mapper.mapToDbModel
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.source.database.data.dao.HabitDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {
    override suspend fun addHabit(habit: Habit) {
        habitDao.insertHabitWithHistoryEntries(habit.mapToDbModel())
    }

}