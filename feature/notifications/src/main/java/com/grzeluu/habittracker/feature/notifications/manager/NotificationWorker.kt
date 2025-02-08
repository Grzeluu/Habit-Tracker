package com.grzeluu.habittracker.feature.notifications.manager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitsNotificationsUseCase
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.Unit
import kotlin.let
import com.grzeluu.habittracker.base.domain.result.Result as CoreResult

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val getHabitsWithNotifications: GetHabitsNotificationsUseCase,
    @Assisted private val getSettingsUseCase: GetSettingsUseCase,
    @Assisted private val appNotificationManager: AppNotificationManager,
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        private const val WORK_NAME = "NotificationWorker"

        fun schedulePeriodicWorker(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder().build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                workRequest
            )
        }
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val settings = getSettingsUseCase.invoke(Unit).firstOrNull() ?: return@withContext Result.failure()

            if (settings is CoreResult.Success && !settings.data.isNotificationsEnabled) return@withContext Result.success()
            getHabitsWithNotifications.invoke(Unit).firstOrNull().let { result ->
                when (result) {
                    is CoreResult.Success -> {
                        Timber.d("NotificationWorker: notifications scheduled")
                        result.data.forEach { habitNotification ->
                            appNotificationManager.scheduleNotification(habitNotification)
                        }
                        Result.success()
                    }

                    is CoreResult.Error -> Result.failure()
                    else -> Result.success()
                }
            }
        }
    }
}