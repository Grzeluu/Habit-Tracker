package com.grzeluu.habittracker.component.habit.data.mapper

import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationEntity
import com.grzeluu.habittracker.source.database.data.model.HabitNotificationWithHabitEntity

fun HabitNotification.mapToEntity() = HabitNotificationEntity(
    habitId = habit.id,
    dateTime = dateTime
)

fun HabitNotificationWithHabitEntity.mapToDomain() = HabitNotification(
    habit = habit.mapToDomain(),
    dateTime = notification.dateTime
)

