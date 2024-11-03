package com.grzeluu.habittracker.activity.ui.navigation.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grzeluu.habittracker.activity.ui.MainPage
import com.grzeluu.habittracker.feature.addhabit.ui.AddHabitScreen
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
                    navController.navigate(NavRoute.AddHabit) {
                        launchSingleTop = true
                        popUpTo(NavRoute.Onboarding) { inclusive = true }
                    }
                }
            )
        }
        composable<NavRoute.MainPage> {
            MainPage(
                onNavigateToAddHabit = {
                    navController.navigate(NavRoute.AddHabit) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<NavRoute.AddHabit> {
            AddHabitScreen(
                onNavigateToMainPage = {
                    navController.navigate(NavRoute.MainPage) {
                        launchSingleTop = true
                        popUpTo(NavRoute.Onboarding) { inclusive = true }
                    }
                }
            )
        }
    }
}