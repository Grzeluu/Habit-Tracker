package com.grzeluu.habittracker.component.settings.di

import com.grzeluu.habittracker.component.settings.data.repository.SettingsRepositoryImpl
import com.grzeluu.habittracker.component.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}