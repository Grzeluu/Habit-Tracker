package com.grzeluu.habittracker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.feature.onboarding.ui.OnboardingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            BaseScreenContainer(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState
            ) { data ->
                HabitTrackerTheme(
                    darkTheme = data.settings?.isDarkModeEnabled ?: isSystemInDarkTheme()
                ) {
                    OnboardingScreen()
                }
            }

        }
    }
}