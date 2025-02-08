package com.grzeluu.habittracker.activity.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.grzeluu.habittracker.activity.ui.navigation.app.AppNavHost
import com.grzeluu.habittracker.activity.ui.navigation.app.NavRoute
import com.grzeluu.habittracker.activity.ui.state.MainActivityStateData
import com.grzeluu.habittracker.base.ui.UiStateScreenContainer
import com.grzeluu.habittracker.base.ui.state.UiState
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.feature.notifications.manager.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            val isDarkMode = (uiState as? UiState.Success<MainActivityStateData>)?.data?.settings?.isDarkModeEnabled ?: isSystemInDarkTheme()

            NotificationWorker.schedulePeriodicWorker(applicationContext)

            HabitTrackerTheme(
                darkTheme = isDarkMode
            ) {
                LaunchedEffect(isDarkMode) {
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !isDarkMode
                }

                UiStateScreenContainer(
                    modifier = Modifier.fillMaxSize(),
                    uiState = uiState
                ) { data ->
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        startDestination = if (data.isSettingsDefinedOnAppStart) NavRoute.MainPage else NavRoute.Onboarding
                    )
                }
            }
        }
    }
}