package com.grzeluu.habittracker.source.preferences.data.datastore

import com.grzeluu.habittracker.source.preferences.data.model.SettingsCacheModel
import kotlinx.coroutines.flow.Flow

interface SettingsDataStore {
    fun getUserSettings(): Flow<SettingsCacheModel?>

    suspend fun saveSettings(settings: SettingsCacheModel)
}