package com.grzeluu.habittracker.feature.details.ui.event

import kotlinx.datetime.LocalDate

sealed class DetailsEvent {
    data class OnSaveDailyEffort(val date: LocalDate, val effort: Float) : DetailsEvent()
    data object OnDeleteHabit : DetailsEvent()
    data object OnArchiveHabit : DetailsEvent()
}