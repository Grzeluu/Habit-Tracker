package com.grzeluu.habittracker.activity.ui.navigation.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grzeluu.habittracker.activity.ui.MainPage
import com.grzeluu.habittracker.feature.addhabit.ui.AddHabitScreen
import com.grzeluu.habittracker.feature.details.ui.DetailsScreen
import com.grzeluu.habittracker.feature.onboarding.ui.OnboardingScreen

@Composable
fun AppNavHost(
    startDestination: NavRoute,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<NavRoute.Onboarding> {
            OnboardingScreen(
                onNavigateToMainPage = {
                    navController.navigate(NavRoute.MainPage) {
                        launchSingleTop = true
                        popUpTo(NavRoute.Onboarding) { inclusive = true }
                    }
                },
                onNavigateToAddHabit = {
                    navController.navigate(NavRoute.MainPage) {
                        launchSingleTop = true
                        popUpTo(NavRoute.Onboarding) { inclusive = true }
                    }
                    navController.navigate(NavRoute.AddHabit()) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavRoute.MainPage> {
            MainPage(
                onNavigateToAddHabit = {
                    navController.navigate(NavRoute.AddHabit()) {
                        launchSingleTop = true
                    }
                },
                onNavigateToDetails = { habitId ->
                    navController.navigate(NavRoute.Details(habitId)) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavRoute.AddHabit> {
            AddHabitScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable<NavRoute.Details> {
            DetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddHabit = { habitId ->
                    navController.navigate(NavRoute.AddHabit(habitId)) {
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}