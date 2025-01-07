package com.grzeluu.habittracker.activity.ui.navigation.main

import androidx.annotation.DrawableRes
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText

sealed class BottomNavItem(val route: String, @DrawableRes val iconResId: Int, val text: UiText) {
    data object Habits : BottomNavItem(
        "home",
        R.drawable.ic_home,
        UiText.StringResource(R.string.daily_habits)
    )

    data object Calendar : BottomNavItem(
        "list",
        R.drawable.ic_list,
        UiText.StringResource(R.string.all_habits)
    )

    data object Notifications :
        BottomNavItem(
            "notifications",
            R.drawable.ic_notification,
            UiText.StringResource(R.string.notifications)
        )

    data object Settings : BottomNavItem(
        "settings",
        R.drawable.ic_settings,
        UiText.StringResource(R.string.settings)
    )
}