package com.grzeluu.habittracker.feature.notifications.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.feature.notifications.manager.AppNotificationManager.Companion.NOTIFICATION_CHANNEL_ID
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val HABIT_ID = "habit_id"
        const val HABIT_TITLE = "habit_title"
        const val ICON_RES_IS = "icon_res_id"

        fun Context.createNotificationIntent(
            habitNotification: HabitNotification,
        ) = Intent(this, NotificationReceiver::class.java).apply {
            putExtra(HABIT_ID, habitNotification.habit.id)
            putExtra(HABIT_TITLE, habitNotification.habit.name)
            putExtra(ICON_RES_IS, habitNotification.habit.icon.mapToDrawableRes())
        }
    }


    override fun onReceive(context: Context, intent: Intent) {
        val habitId = requireNotNull(intent.getLongExtra(HABIT_ID, -1))
        val title = requireNotNull(intent.getStringExtra(HABIT_TITLE))
        val iconResId = requireNotNull(intent.getIntExtra(ICON_RES_IS, -1))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Timber.d("Notify for habitId = $habitId")
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(iconResId)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.its_time_to_check_your_habit))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(habitId.toInt(), notification)
    }
}