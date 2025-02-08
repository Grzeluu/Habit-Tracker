package com.grzeluu.habittracker

import android.app.Application
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class HabitTrackerApp @Inject constructor() : Application() {

    @Inject
    lateinit var notificationManager: NotificationScheduler

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        notificationManager.initNotificationChannel()
    }
}

