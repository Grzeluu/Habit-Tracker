package com.grzeluu.habittracker.activity.ui.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grzeluu.habittracker.feature.calendar.ui.HabitsListScreen
import com.grzeluu.habittracker.feature.home.ui.HabitsScreen
import com.grzeluu.habittracker.feature.notifications.ui.NotificationsScreen
import com.grzeluu.habittracker.feature.settings.ui.SettingsScreen


@Composable
fun MainPageNavigationHost(
    navController: NavHostController,
    modifier: Modifier,
    onNavigateToDetails: (habitId: Long) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavItem.Habits.route
    ) {
        composable(BottomNavItem.Habits.route) { HabitsScreen(onNavigateToDetails) }
        composable(BottomNavItem.Calendar.route) { HabitsListScreen(onNavigateToDetails) }
        composable(BottomNavItem.Notifications.route) { NotificationsScreen() }
        composable(BottomNavItem.Settings.route) { SettingsScreen() }
    }
}