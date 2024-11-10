package com.grzeluu.habittracker.common.ui.mapper

import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.util.enums.Day


fun Day.mapToUiText(): UiText =
    when (this) {
        Day.MONDAY -> UiText.StringResource(R.string.monday_short)
        Day.TUESDAY -> UiText.StringResource(R.string.tuesday_short)
        Day.WEDNESDAY -> UiText.StringResource(R.string.wednesday_short)
        Day.THURSDAY -> UiText.StringResource(R.string.thursday_short)
        Day.FRIDAY -> UiText.StringResource(R.string.friday_short)
        Day.SATURDAY -> UiText.StringResource(R.string.saturday_short)
        Day.SUNDAY -> UiText.StringResource(R.string.sunday_short)
    }