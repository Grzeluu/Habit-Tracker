package com.grzeluu.habittracker.component.habit.domain.model

import com.grzeluu.habittracker.util.enums.EffortUnit

data class HabitDesiredEffort(
    val effortUnit: EffortUnit,
    val desired: Float,
)