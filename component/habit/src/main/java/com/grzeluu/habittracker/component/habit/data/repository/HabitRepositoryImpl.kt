package com.grzeluu.habittracker.component.habit.data.repository

import com.grzeluu.habittracker.component.habit.data.mapper.mapToDomain
import com.grzeluu.habittracker.component.habit.data.mapper.mapToEntity
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.source.database.data.dao.HabitDao
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {
    override fun getHabits(): Flow<List<Habit>> {
        return habitDao.getHabits().map { it.map { habit -> habit.mapToDomain() } }
    }

    override fun getHabit(habitId: Long): Flow<Habit?> {
        return habitDao.getHabitWithHistoryEntriesByHabitId(habitId).map { it?.mapToDomain() }
    }

    override fun getDailyHabitInfos(day: Day, dateTime: LocalDate): Flow<List<DailyHabitInfo>> {
        return habitDao.getHabitsWithDailyEntryByDayAndDate(day.name, dateTime).map {
            it.map { habit ->
                habit.mapToDomain()
            }
        }
    }

    override fun getHabitsCount(): Flow<Int> {
        return habitDao.getHabitsCount()
    }

    override suspend fun addOrUpdateHabit(habit: Habit): Long {
        return if (habit.id == 0L) {
            habitDao.insertHabit(habit.mapToEntity())
        } else {
            habitDao.updateHabit(habit.mapToEntity())
            habit.id
        }
    }

    override suspend fun addHabitHistoryEntry(habitId: Long, habitHistoryEntry: HabitHistoryEntry) {
        habitDao.insertHabitHistoryEntry(habitHistoryEntry.mapToEntity(habitId))
    }

    override suspend fun deleteHabit(habit: Habit) {
        return habitDao.deleteHabit(habit.mapToEntity())
    }

    override suspend fun markHabitAsArchived(habitId: Long, isArchived: Boolean) {
        return habitDao.markHabitAsArchived(habitId, isArchived)
    }
}