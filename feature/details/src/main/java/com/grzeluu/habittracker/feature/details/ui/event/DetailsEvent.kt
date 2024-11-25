package com.grzeluu.habittracker.feature.details.ui.event

sealed class DetailsEvent {
    data object onArchiveHabit : DetailsEvent()
    data object onDeleteHabit : DetailsEvent()
}