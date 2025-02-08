package com.grzeluu.habittracker.component.habit.di

import com.grzeluu.habittracker.component.habit.data.repository.HabitRepositoryImpl
import com.grzeluu.habittracker.component.habit.data.repository.NotificationRepositoryImpl
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationManager
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationManagerImpl
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationSchedulerImpl
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
    abstract fun bindHabitsRepository(repository: HabitRepositoryImpl): HabitRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(repository: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindNotificationManager(scheduler: NotificationManagerImpl): NotificationManager

    @Binds
    @Singleton
    abstract fun bindNotificationScheduler(scheduler: NotificationSchedulerImpl): NotificationScheduler
}