package com.grzeluu.habittracker.activity.ui.navigation.app

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Onboarding : NavRoute

    @Serializable
    data object MainPage : NavRoute

    @Serializable
    data object AddHabit : NavRoute

}