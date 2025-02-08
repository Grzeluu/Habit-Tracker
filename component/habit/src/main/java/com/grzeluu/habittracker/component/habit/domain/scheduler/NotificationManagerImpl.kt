package com.grzeluu.habittracker.component.habit.domain.scheduler

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManagerImpl @Inject constructor(
) : NotificationManager {


    override suspend fun calculateNextNotificationForHabit(habit: Habit): HabitNotification {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        if (habit.notification !is HabitNotificationSetting.Enabled) throw RuntimeException("Habit notification is already disabled")
        var notificationDateTime = LocalDateTime(currentTime.date, habit.notification.time)

        while (notificationDateTime <= currentTime || !habit.desirableDays.contains(Day.get(notificationDateTime.dayOfWeek.value))) {
            notificationDateTime = LocalDateTime(notificationDateTime.date.plus(1, DateTimeUnit.DAY), habit.notification.time)
        }

        return HabitNotification(
            habit = habit,
            dateTime = notificationDateTime
        )
    }
}