package com.grzeluu.habittracker.feature.onboarding.ui.intent

sealed class OnboardingIntent {
    data class SetIsDarkModeEnabled(val isEnabled: Boolean): OnboardingIntent()
    data class SetIsNotificationsEnabled(val isEnabled: Boolean): OnboardingIntent()
}