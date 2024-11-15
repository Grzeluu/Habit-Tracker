package com.grzeluu.habittracker.component.habit.data.mapper

import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity
import com.grzeluu.habittracker.source.database.data.model.HabitWithHistoryDbModel

fun Habit.mapToDbModel(): HabitWithHistoryDbModel =
    HabitWithHistoryDbModel(
        habit = mapToEntity(),
        historyEntries = history.map { it.mapToEntity(id) }

    )

private fun Habit.mapToEntity() =
    HabitEntity(
        id = id,
        name = name,
        iconValue = icon.name,
        colorValue = color.name,
        description = description,
        desirableDays = desirableDays?.map { it.name },
        isNotificationEnabled = habitNotification is HabitNotification.Enabled,
        notificationTime = if (habitNotification is HabitNotification.Enabled) habitNotification.time else null,
        effortUnit = effort.effortUnit.name,
        desiredEffortValue = effort.desiredValue,
        isArchive = isArchive
    )

fun HabitHistoryEntry.mapToEntity(habitId: Long) =
    HabitHistoryEntryEntity(
        habitId = habitId,
        date = date,
        currentEffort = currentEffort,
        note = note
    )

