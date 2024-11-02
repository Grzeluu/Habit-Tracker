package com.grzeluu.habittracker.component.settings.domain.model

data class Settings(
    val isDarkModeEnabled: Boolean,
    val isNotificationsEnabled: Boolean
) {
    companion object {
        val DEFAULT = Settings(
            isDarkModeEnabled = false,
            isNotificationsEnabled = false
        )
    }
}