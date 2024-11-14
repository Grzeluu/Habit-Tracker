package com.grzeluu.habittracker.source.database.di

import android.content.Context
import androidx.room.Room
import com.grzeluu.habittracker.source.database.HabitTrackerDatabase
import com.grzeluu.habittracker.source.database.data.dao.HabitDao
import com.grzeluu.habittracker.source.database.data.dao.HabitEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): HabitTrackerDatabase {
        return Room.databaseBuilder(
            appContext,
            HabitTrackerDatabase::class.java,
            "Habit_tracker_database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideHabitDao(appDatabase: HabitTrackerDatabase) : HabitDao{
        return appDatabase.habitDao()
    }
    @Provides
    @Singleton
    fun provideHabitEntryDao(appDatabase: HabitTrackerDatabase) : HabitEntryDao{
        return appDatabase.habitEntryDao()
    }

}