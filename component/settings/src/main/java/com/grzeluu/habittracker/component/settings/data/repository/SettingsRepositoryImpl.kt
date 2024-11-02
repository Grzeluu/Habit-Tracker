package com.grzeluu.habittracker.component.settings.data.repository

import com.grzeluu.habittracker.component.settings.data.mapper.mapToCacheModel
import com.grzeluu.habittracker.component.settings.data.mapper.mapToDomain
import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.component.settings.domain.repository.SettingsRepository
import com.grzeluu.habittracker.source.preferences.data.datastore.SettingsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataSource: SettingsDataSource
) : SettingsRepository {

    override fun getSettings(): Flow<Settings?> {
        return settingsDataSource.getUserSettings().map {
            it?.mapToDomain()
        }
    }

    override suspend fun saveSettings(settings: Settings) {
        settingsDataSource.saveSettings(settings.mapToCacheModel())
    }

}