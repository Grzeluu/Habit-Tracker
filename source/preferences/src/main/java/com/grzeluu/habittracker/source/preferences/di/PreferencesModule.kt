package com.grzeluu.habittracker.source.preferences.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.grzeluu.habittracker.source.preferences.data.datastore.SettingsDataSource
import com.grzeluu.habittracker.source.preferences.data.datastore.SettingsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindSettingsDataSource(manager: SettingsDataSourceImpl): SettingsDataSource

    @Module
    @InstallIn(SingletonComponent::class)
    object StaticModule {

        private const val USER_PREFERENCES = "user_settings_preferences"

        @Singleton
        @Provides
        @SettingsStore
        fun provideSettingsPreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
            )
        }

    }

}