package com.grzeluu.habittracker.component.habit.data.mapper

import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.source.database.data.model.HabitEntity
import com.grzeluu.habittracker.source.database.data.model.HabitHistoryEntryEntity
import com.grzeluu.habittracker.source.database.data.model.HabitWithHistoryDbModel
import com.grzeluu.habittracker.source.database.data.model.HabitWithOneDayHistoryEntryDbModel
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit

fun Habit.mapToDbModel(): HabitWithHistoryDbModel =
    HabitWithHistoryDbModel(
        habit = mapToEntity(),
        historyEntries = history.map { it.mapToEntity(id) }

    )

fun Habit.mapToEntity() =
    HabitEntity(
        id = id,
        name = name,
        iconValue = icon.name,
        colorValue = color.name,
        description = description,
        desirableDays = desirableDays.map { it.name },
        isNotificationEnabled = notification is HabitNotificationSetting.Enabled,
        notificationTime = if (notification is HabitNotificationSetting.Enabled) notification.time else null,
        effortUnit = effort.effortUnit.name,
        desiredEffortValue = effort.desiredValue,
        additionDate = additionDate,
        isArchive = isArchive,
    )

fun HabitHistoryEntry.mapToEntity(habitId: Long) =
    HabitHistoryEntryEntity(
        habitId = habitId,
        date = date,
        currentEffort = currentEffort,
        note = note
    )

fun HabitHistoryEntryEntity.mapToDomain() =
    HabitHistoryEntry(
        date = date,
        currentEffort = currentEffort,
        note = note
    )

fun HabitWithOneDayHistoryEntryDbModel.mapToDomain() = DailyHabitInfo(
    id = habit.id,
    name = habit.name,
    icon = CardIcon.valueOf(habit.iconValue),
    color = CardColor.valueOf(habit.colorValue),
    description = habit.description,
    effort = HabitDesiredEffort(
        effortUnit = EffortUnit.valueOf(habit.effortUnit),
        desiredValue = habit.desiredEffortValue
    ),
    dailyHistoryEntry = historyEntry?.mapToDomain()
)

fun HabitWithHistoryDbModel.mapToDomain() = Habit(
    id = habit.id,
    name = habit.name,
    icon = CardIcon.valueOf(habit.iconValue),
    color = CardColor.valueOf(habit.colorValue),
    description = habit.description,
    effort = HabitDesiredEffort(
        effortUnit = EffortUnit.valueOf(habit.effortUnit),
        desiredValue = habit.desiredEffortValue
    ),
    history = historyEntries.map { it.mapToDomain() },
    notification = if (habit.isNotificationEnabled) HabitNotificationSetting.Enabled(habit.notificationTime!!) else HabitNotificationSetting.Disabled,
    additionDate = habit.additionDate,
    isArchive = habit.isArchive,
    desirableDays = habit.desirableDays.map { Day.valueOf(it) }
)

