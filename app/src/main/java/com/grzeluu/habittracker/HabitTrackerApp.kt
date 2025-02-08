package com.grzeluu.habittracker

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.grzeluu.habittracker.activity.di.NotificationWorkerFactory
import com.grzeluu.habittracker.feature.notifications.manager.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class HabitTrackerApp @Inject constructor() : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: NotificationWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

