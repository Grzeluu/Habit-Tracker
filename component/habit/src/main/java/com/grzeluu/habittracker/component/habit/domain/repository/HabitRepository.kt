package com.grzeluu.habittracker.component.habit.domain.repository

import com.grzeluu.habittracker.component.habit.domain.model.Habit

interface HabitRepository {
    suspend fun addHabit(habit: Habit)
}