package com.grzeluu.habittracker.component.habit.di

import com.grzeluu.habittracker.component.habit.data.repository.HabitRepositoryImpl
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HabitModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(repository: HabitRepositoryImpl): HabitRepository
}