package com.grzeluu.habittracker.feature.home.ui.event

import kotlinx.datetime.LocalDate

sealed class HomeEvent {
    data class OnChangeSelectedDay(val dateTime: LocalDate) : HomeEvent()
}