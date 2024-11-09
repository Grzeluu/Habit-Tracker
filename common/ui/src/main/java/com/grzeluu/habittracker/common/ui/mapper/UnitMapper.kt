package com.grzeluu.habittracker.common.ui.mapper

import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText


fun EffortUnit.mapToUiText(): UiText =
    when (this) {
        EffortUnit.MINUTE -> UiText.StringResource(R.string.minute)
        EffortUnit.HOUR -> UiText.StringResource(R.string.hour)
        EffortUnit.KM -> UiText.StringResource(R.string.km)
        EffortUnit.KCAL -> UiText.StringResource(R.string.kcal)
        EffortUnit.LITERS -> UiText.StringResource(R.string.liters)
        EffortUnit.REPEAT -> UiText.Empty
        EffortUnit.STEPS -> UiText.StringResource(R.string.steps)
    }