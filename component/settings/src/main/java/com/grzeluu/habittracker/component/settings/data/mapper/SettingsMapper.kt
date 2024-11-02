package com.grzeluu.habittracker.component.settings.data.mapper

import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.source.preferences.data.model.SettingsCacheModel

fun Settings.mapToCacheModel(): SettingsCacheModel =
    SettingsCacheModel(
        isDarkMode = isDarkModeEnabled,
        isNotificationsEnabled = isNotificationsEnabled
    )

fun SettingsCacheModel.mapToDomain(): Settings =
    Settings(
        isDarkModeEnabled = isDarkMode,
        isNotificationsEnabled = isNotificationsEnabled
    )