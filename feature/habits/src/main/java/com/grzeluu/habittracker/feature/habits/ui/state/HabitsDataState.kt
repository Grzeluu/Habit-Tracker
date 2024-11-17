package com.grzeluu.habittracker.feature.habits.ui.state

import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

data class HabitsDataState(
    val daysOfWeek: List<Pair<Day, LocalDate>>,
    val selectedDay: LocalDate,
    val dailyHabits: List<DailyHabitInfo>,
    val dailyStatistics: DailyStatisticsData,
)

data class DailyStatisticsData(
    val totalHabits: Int,
    val totalProgress: Float,
    val habitsDone: Int,
)