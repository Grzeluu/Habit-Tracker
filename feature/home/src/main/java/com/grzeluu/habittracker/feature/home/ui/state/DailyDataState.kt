package com.grzeluu.habittracker.feature.home.ui.state

import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo

data class DailyDataState(
    val dailyHabits: List<DailyHabitInfo>,
    val dailyStatistics: DailyStatisticsData,
)

data class DailyStatisticsData(
    val totalHabits: Int,
    val totalProgress: Float,
    val habitsDone: Int,
)