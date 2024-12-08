package com.grzeluu.habittracker.feature.details.ui.state

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import kotlinx.datetime.LocalDate


data class DetailsDataState(
    val habit: Habit,
    val lastDays: List<LocalDate>,
)