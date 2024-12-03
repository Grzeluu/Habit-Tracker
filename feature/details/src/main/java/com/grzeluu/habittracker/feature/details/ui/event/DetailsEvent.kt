package com.grzeluu.habittracker.feature.details.ui.event

sealed class DetailsEvent {
    data object OnDeleteHabit : DetailsEvent()
    data object OnArchiveHabit : DetailsEvent()
}