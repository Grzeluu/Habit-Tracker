package com.grzeluu.habittracker.feature.onboarding.ui.event

sealed class OnboardingEvent {
    data class OnChangeDarkMode(val isDarkModeEnabled: Boolean) : OnboardingEvent()
    data class OnChangeNotifications(val isNotificationsEnabled: Boolean) : OnboardingEvent()
    data class OnContinue(val destinationNavigationEvent: OnboardingNavigationEvent) : OnboardingEvent()
}