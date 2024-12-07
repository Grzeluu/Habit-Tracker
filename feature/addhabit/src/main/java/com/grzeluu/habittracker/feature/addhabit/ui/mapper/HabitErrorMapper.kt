package com.grzeluu.habittracker.feature.addhabit.ui.mapper

import com.grzeluu.habittracker.base.domain.result.RootError
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.component.habit.domain.error.HabitValidationError
import com.grzeluu.habittracker.common.ui.R

fun RootError.asUiText(): UiText = when (this) {
    HabitValidationError.EMPTY_NAME -> UiText.StringResource(R.string.habit_validation_error_empty_name)
    HabitValidationError.EMPTY_DAYS -> UiText.StringResource(R.string.habit_validation_error_empty_days)
    else -> UiText.StringResource(R.string.something_has_gone_wrong)
}