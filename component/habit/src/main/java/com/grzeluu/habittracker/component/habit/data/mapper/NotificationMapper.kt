package com.grzeluu.habittracker.component.habit.data.mapper

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationEntity

fun HabitNotification.mapToEntity() = HabitNotificationEntity(
    habitId = habitId,
    dateTime = dateTime
)