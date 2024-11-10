package com.grzeluu.habittracker.common.ui.mapper

import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText

enum class MappingType {
    ABBREVIATION,
    PLURAL,
    SINGULAR
}

fun EffortUnit.mapToUiText(mappingType: MappingType = MappingType.ABBREVIATION): UiText =
    when (this) {
        EffortUnit.MINUTES -> when (mappingType) {
            MappingType.ABBREVIATION -> UiText.StringResource(R.string.minute_abr)
            MappingType.PLURAL -> UiText.StringResource(R.string.minutes)
            MappingType.SINGULAR -> UiText.StringResource(R.string.minute)
        }

        EffortUnit.HOURS -> when (mappingType) {
            MappingType.ABBREVIATION -> UiText.StringResource(R.string.hour_abr)
            MappingType.PLURAL -> UiText.StringResource(R.string.hours)
            MappingType.SINGULAR -> UiText.StringResource(R.string.hour)
        }

        EffortUnit.KM -> when (mappingType) {
            MappingType.ABBREVIATION -> UiText.StringResource(R.string.kilometer_abr)
            MappingType.PLURAL -> UiText.StringResource(R.string.kilometers)
            MappingType.SINGULAR -> UiText.StringResource(R.string.kilometer)
        }

        EffortUnit.KCAL -> {
            UiText.StringResource(R.string.kcal)
        }

        EffortUnit.LITERS -> {
            when (mappingType) {
                MappingType.ABBREVIATION ->UiText.StringResource(R.string.liters_abr)
                MappingType.PLURAL -> UiText.StringResource(R.string.liters)
                MappingType.SINGULAR -> UiText.StringResource(R.string.liter)
            }
        }

        EffortUnit.TIMES -> when (mappingType) {
            MappingType.ABBREVIATION -> UiText.Empty
            MappingType.PLURAL -> UiText.StringResource(R.string.times)
            MappingType.SINGULAR -> UiText.StringResource(R.string.time)
        }

        EffortUnit.STEPS -> {
            UiText.StringResource(R.string.steps)
        }
    }

