package com.grzeluu.habittracker.source.preferences.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.grzeluu.habittracker.source.preferences.data.model.SettingsCacheModel
import com.grzeluu.habittracker.source.preferences.di.SettingsStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataSourceImpl @Inject constructor(
    @SettingsStore private val settingsDataStore: DataStore<Preferences>
) : SettingsDataSource {

    companion object {
        private const val SETTINGS_KEY = ("SETTINGS")
        private fun getKey() = stringPreferencesKey(SETTINGS_KEY)
    }

    override fun getUserSettings(): Flow<SettingsCacheModel?> = settingsDataStore.data.map { prefs ->
        prefs[getKey()]?.let { data ->
            Json.decodeFromString(data)
        }
    }

    override suspend fun saveSettings(settings: SettingsCacheModel) {
        settingsDataStore.edit { prefs ->
            prefs[getKey()] = Json.encodeToString(settings)
        }
    }

}