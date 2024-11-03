package com.grzeluu.habittracker.activity.ui.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grzeluu.habittracker.feature.calendar.ui.CalendarScreen
import com.grzeluu.habittracker.feature.habits.ui.HabitsScreen
import com.grzeluu.habittracker.feature.notifications.ui.NotificationsScreen
import com.grzeluu.habittracker.feature.settings.ui.SettingsScreen


@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavItem.Habits.route
    ) {
        composable(BottomNavItem.Habits.route) { HabitsScreen() }
        composable(BottomNavItem.Calendar.route) { CalendarScreen() }
        composable(BottomNavItem.Notifications.route) { NotificationsScreen() }
        composable(BottomNavItem.Settings.route) { SettingsScreen() }
    }
}