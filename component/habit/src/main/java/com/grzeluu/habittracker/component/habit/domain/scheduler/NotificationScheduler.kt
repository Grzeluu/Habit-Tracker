package com.grzeluu.habittracker.component.habit.domain.scheduler

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification

interface NotificationScheduler {
    suspend fun calculateNextNotificationForHabit(habit: Habit): HabitNotification
}
