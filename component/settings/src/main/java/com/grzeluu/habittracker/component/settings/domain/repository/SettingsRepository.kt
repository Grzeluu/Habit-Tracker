package com.grzeluu.habittracker.component.settings.domain.repository

import com.grzeluu.habittracker.component.settings.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings?>

    suspend fun saveSettings(settings: Settings)
}