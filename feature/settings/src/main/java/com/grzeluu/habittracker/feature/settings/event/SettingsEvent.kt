package com.grzeluu.habittracker.feature.settings.event

sealed class SettingsEvent {
    data class OnChangeDarkMode(val isDarkModeEnabled: Boolean) : SettingsEvent()
    data class OnChangeNotifications(val isEnabled: Boolean) : SettingsEvent()
}