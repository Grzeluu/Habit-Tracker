package com.grzeluu.habittracker.component.habit.di

import com.grzeluu.habittracker.component.habit.data.repository.HabitRepositoryImpl
import com.grzeluu.habittracker.component.habit.data.repository.NotificationRepositoryImpl
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationScheduler
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HabitModule {
    @Binds
    @Singleton
    abstract fun bindHabitsRepository(repository: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(repository: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(scheduler: NotificationSchedulerImpl): NotificationScheduler
}