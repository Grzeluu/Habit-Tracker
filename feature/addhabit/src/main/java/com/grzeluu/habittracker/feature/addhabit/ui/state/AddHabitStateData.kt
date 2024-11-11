package com.grzeluu.habittracker.feature.addhabit.ui.state

import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit

data class AddHabitStateData(
    val name: String,
    val description: String,
    val color: CardColor,
    val icon: CardIcon,
    val selectedDays: List<Day>,
    val dailyGoal: Float,
    val effortUnit: EffortUnit,
    val isNotificationsEnabled: Boolean
)