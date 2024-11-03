package com.grzeluu.habittracker.activity.ui.navigation.main

import androidx.annotation.DrawableRes
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText

sealed class BottomNavItem(val route: String, @DrawableRes val iconResId: Int, val text: UiText) {
    data object Habits : BottomNavItem(
        "habits",
        R.drawable.ic_home,
        UiText.StringResource(R.string.habits)
    )

    data object Calendar : BottomNavItem(
        "calendar",
        R.drawable.ic_calendar,
        UiText.StringResource(R.string.calendar)
    )

    data object Notifications :
        BottomNavItem(
            "notifications",
            R.drawable.ic_notification,
            UiText.StringResource(R.string.notification)
        )

    data object Settings : BottomNavItem(
        "settings",
        R.drawable.ic_settings,
        UiText.StringResource(R.string.settings)
    )
}