package com.grzeluu.habittracker.feature.details.ui.enum

import androidx.annotation.StringRes
import com.grzeluu.habittracker.common.ui.R

enum class ProgressPeriod(@StringRes val label: Int) {
    WEEK(R.string.week),
    MONTH(R.string.month),
    YEAR(R.string.year),
}