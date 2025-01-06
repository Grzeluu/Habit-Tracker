package com.grzeluu.habittracker.feature.details.ui.event

import com.grzeluu.habittracker.feature.details.ui.enum.ProgressPeriod
import kotlinx.datetime.LocalDate

sealed class DetailsEvent {
    data class OnSaveDailyEffort(val date: LocalDate, val effort: Float) : DetailsEvent()
    data object OnDeleteHabit : DetailsEvent()
    data object OnArchiveHabit : DetailsEvent()
    data class OnSelectPeriod(val period: ProgressPeriod) : DetailsEvent()
}