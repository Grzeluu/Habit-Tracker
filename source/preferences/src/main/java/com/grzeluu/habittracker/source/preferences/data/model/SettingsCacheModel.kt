package com.grzeluu.habittracker.source.preferences.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsCacheModel(
    val isDarkMode: Boolean,
    val isNotificationsEnabled: Boolean
)