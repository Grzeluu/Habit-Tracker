package com.grzeluu.habittracker.feature.notifications.manager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.feature.notifications.manager.NotificationReceiver.Companion.createNotificationIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "habit_notifications"
    }

    init {
        notificationManager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.habit_notifications),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.habit_notifications_channel_desc)
            }
        )
    }

    fun scheduleNotification(habitNotification: HabitNotification) {
        Timber.d("scheduleNotification for habitId = ${habitNotification.habit.id}")

        val intent = context.createNotificationIntent(habitNotification)

        val pendingIntent = PendingIntent.getBroadcast(
            context, habitNotification.habit.id.toInt(), intent, PendingIntent.FLAG_MUTABLE
        )

        val triggerTime = habitNotification.dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}
