package com.grzeluu.habittracker.activity.ui.navigation.app

import com.grzeluu.habittracker.feature.addhabit.ui.navigation.AddHabitArgument
import com.grzeluu.habittracker.feature.details.ui.navigation.DetailsArguments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Onboarding : NavRoute

    @Serializable
    data object MainPage : NavRoute

    @Serializable
    data class AddHabit(
        @SerialName(AddHabitArgument.HABIT_ID)
        val habitId: Long? = null
    ) : NavRoute

    @Serializable
    data class Details(
        @SerialName(DetailsArguments.HABIT_ID)
        val habitId: Long
    ) : NavRoute

}