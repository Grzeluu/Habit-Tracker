package com.grzeluu.habittracker.common.ui.mapper

import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.util.enums.Day


fun Day.mapToUiText(isShort: Boolean = true): UiText {
    val stringResourceMap = if (isShort) {
        mapOf(
            Day.MONDAY to R.string.monday_short,
            Day.TUESDAY to R.string.tuesday_short,
            Day.WEDNESDAY to R.string.wednesday_short,
            Day.THURSDAY to R.string.thursday_short,
            Day.FRIDAY to R.string.friday_short,
            Day.SATURDAY to R.string.saturday_short,
            Day.SUNDAY to R.string.sunday_short
        )
    } else {
        mapOf(
            Day.MONDAY to R.string.monday,
            Day.TUESDAY to R.string.tuesday,
            Day.WEDNESDAY to R.string.wednesday,
            Day.THURSDAY to R.string.thursday,
            Day.FRIDAY to R.string.friday,
            Day.SATURDAY to R.string.saturday,
            Day.SUNDAY to R.string.sunday
        )
    }
    return UiText.StringResource(stringResourceMap[this] ?: R.string.monday)
}