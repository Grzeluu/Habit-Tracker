package com.grzeluu.habittracker.common.ui.mapper

import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.common.ui.R

fun CardIcon.mapToDrawableRes(): Int =
        when (this) {
            CardIcon.GOAL -> R.drawable.ic_goal
            CardIcon.WELLNESS -> R.drawable.ic_wellness
            CardIcon.EXERCISE -> R.drawable.ic_exercise
            CardIcon.SLEEP -> R.drawable.ic_sleep
            CardIcon.BIKE -> R.drawable.ic_bike
            CardIcon.RUN -> R.drawable.ic_run
            CardIcon.MUSIC -> R.drawable.ic_music
            CardIcon.BOOK -> R.drawable.ic_read
            CardIcon.FOOD -> R.drawable.ic_food
            CardIcon.DRINK -> R.drawable.ic_drink
        }