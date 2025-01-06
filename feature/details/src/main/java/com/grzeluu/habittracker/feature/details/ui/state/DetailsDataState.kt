package com.grzeluu.habittracker.feature.details.ui.state

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.feature.details.ui.enum.ProgressPeriod
import kotlinx.datetime.LocalDate


data class DetailsDataState(
    val habit: Habit,
    val lastDays: List<LocalDate>,
    val selectedPeriod: ProgressPeriod,
    val periodStats: List<Pair<Float, String>>,
)