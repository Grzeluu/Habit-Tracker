package com.grzeluu.habittracker.activity.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grzeluu.habittracker.activity.ui.components.BottomNavigationBar
import com.grzeluu.habittracker.activity.ui.navigation.main.BottomNavItem
import com.grzeluu.habittracker.activity.ui.navigation.main.MainPageNavigationHost

@Composable
fun MainPage(modifier: Modifier = Modifier, onNavigateToAddHabit: () -> Unit) {

    val mainPageNavController = rememberNavController()
    val navBackStackEntry by mainPageNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = remember {
        listOf(
            BottomNavItem.Habits,
            BottomNavItem.Calendar,
            BottomNavItem.Notifications,
            BottomNavItem.Settings,
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                navigationItems = bottomNavItems,
                currentItemRoute = currentRoute,
                onNavigationItemClick = {
                    mainPageNavController.navigate(it.route) {
                        popUpTo(mainPageNavController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        MainPageNavigationHost(
            modifier = Modifier.padding(innerPadding),
            navController = mainPageNavController
        )
    }
}