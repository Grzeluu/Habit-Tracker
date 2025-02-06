package com.grzeluu.habittracker.component.habit.domain.scheduler

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationSchedulerImpl @Inject constructor(
    private val habitRepository: HabitRepository
) : NotificationScheduler {

    override suspend fun calculateNextNotificationForHabit(habitId: Long): HabitNotification {
        val habit = habitRepository.getHabit(habitId).firstOrNull()!!

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val currentDay = Day.get(today.dayOfWeek.value)

        val nextDay = habit.desirableDays
            .map { it.value }
            .firstOrNull { it >= currentDay.value }
            ?: habit.desirableDays.first().value

        val daysUntilNext = (nextDay - currentDay.value).let { if (it <= 0) it + 7 else it }
        val nextDate = today.plus(daysUntilNext, DateTimeUnit.DAY)

        if (habit.notification !is HabitNotificationSetting.Enabled) throw RuntimeException("Habit notification is already disabled")

        return HabitNotification(
            habitId = habitId,
            dateTime = LocalDateTime(nextDate, habit.notification.time)

        )
    }
}