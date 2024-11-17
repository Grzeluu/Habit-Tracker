package com.grzeluu.habittracker.feature.habits.ui.event

import kotlinx.datetime.LocalDate

sealed class HabitsEvent {
    data class OnChangeSelectedDay(val dateTime: LocalDate) : HabitsEvent()
}