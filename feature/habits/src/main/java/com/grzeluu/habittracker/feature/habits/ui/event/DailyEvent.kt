package com.grzeluu.habittracker.feature.habits.ui.event

sealed class DailyEvent {
    data class OnSaveDailyEffort(val habitId: Long, val effort: Float) : DailyEvent()
}