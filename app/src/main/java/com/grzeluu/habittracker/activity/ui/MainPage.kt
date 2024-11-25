package com.grzeluu.habittracker.activity.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grzeluu.habittracker.activity.ui.animation.MainPageAnimations
import com.grzeluu.habittracker.activity.ui.components.BottomNavigationBar
import com.grzeluu.habittracker.activity.ui.navigation.main.BottomNavItem
import com.grzeluu.habittracker.activity.ui.navigation.main.MainPageNavigationHost
import com.grzeluu.habittracker.common.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    onNavigateToAddHabit: () -> Unit,
    onNavigateToDetails: (habitId: Long) -> Unit
) {

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
        topBar = { TopAppBar(title = { Text("Your goals") }) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute == BottomNavItem.Habits.route,
                enter = MainPageAnimations.enterFAB,
                exit = MainPageAnimations.exitFAB
            ) {
                FloatingActionButton(onClick = onNavigateToAddHabit) {
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
                }
            }
        },
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
            navController = mainPageNavController,
            onNavigateToDetails = onNavigateToDetails
        )
    }
}