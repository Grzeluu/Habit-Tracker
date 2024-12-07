package com.grzeluu.habittracker.component.habit.domain.error

import com.grzeluu.habittracker.base.domain.result.RootError

enum class HabitValidationError : RootError {
    EMPTY_NAME,
    EMPTY_DAYS
}