package com.grzeluu.habittracker.feature.onboarding.ui


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnboardingScreen() {

    val viewModel: OnboardingViewModel by viewModels()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    ) {

    }
}