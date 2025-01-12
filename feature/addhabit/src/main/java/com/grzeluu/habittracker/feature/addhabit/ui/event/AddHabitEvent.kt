package com.grzeluu.habittracker.feature.addhabit.ui.event

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit
import kotlinx.datetime.LocalTime

sealed class AddHabitEvent {
    data class OnNameChanged(val value: String) : AddHabitEvent()
    data class OnDescriptionChanged(val value: String) : AddHabitEvent()
    data class OnColorChanged(val value: CardColor) : AddHabitEvent()
    data class OnIconChanged(val icon: CardIcon) : AddHabitEvent()
    data class OnDayChanged(val day: Day, val isChecked: Boolean) : AddHabitEvent()
    data object OnAllDaysToggled : AddHabitEvent()
    data class OnDailyGoalTextChanged(val value: String) : AddHabitEvent()
    data class OnDailyGoalUnitChanged(val unit: EffortUnit) : AddHabitEvent()
    data class OnNotificationsEnabledChanged(val value: Boolean) : AddHabitEvent()
    data class  OnNotificationTimeChanged(val time: LocalTime) : AddHabitEvent()
    data object AddHabit : AddHabitEvent()
}