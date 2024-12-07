package com.grzeluu.habittracker.feature.addhabit.ui.state

import com.grzeluu.habittracker.common.ui.state.FieldState
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit

data class AddHabitDataState(
    val nameField: FieldState<String>,
    val description: String?,
    val color: CardColor,
    val icon: CardIcon,
    val selectedDaysField: FieldState<List<Day>>,
    val dailyEffort: String?,
    val effortUnit: EffortUnit,
    val isNotificationsEnabled: Boolean
)