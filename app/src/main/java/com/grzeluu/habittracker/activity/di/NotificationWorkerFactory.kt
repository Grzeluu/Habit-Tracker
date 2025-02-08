package com.grzeluu.habittracker.activity.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitsNotificationsUseCase
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import com.grzeluu.habittracker.feature.notifications.manager.AppNotificationManager
import com.grzeluu.habittracker.feature.notifications.manager.NotificationWorker
import javax.inject.Inject


class NotificationWorkerFactory @Inject constructor(
    private val getHabitsWithNotifications: GetHabitsNotificationsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val appNotificationManager: AppNotificationManager,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker =
        NotificationWorker(
            getHabitsWithNotifications,
            getSettingsUseCase,
            appNotificationManager,
            appContext,
            workerParameters
        )

}
