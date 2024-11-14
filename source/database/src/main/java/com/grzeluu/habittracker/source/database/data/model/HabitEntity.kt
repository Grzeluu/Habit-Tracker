package com.grzeluu.habittracker.source.database.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon_value")
    val iconValue: String,
    @ColumnInfo(name = "color_value")
    val colorValue: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "desirable_days")
    val desirableDays: List<String>?,
    @ColumnInfo(name = "notification_hour")
    val notificationHour: String?,
    @ColumnInfo(name = "notification_minute")
    val notificationMinute: String?,
    @ColumnInfo(name = "effort_unit_value")
    val effortUnitValue: String,
    @ColumnInfo(name = "desired_effort")
    val desiredEffort: Float,
    @ColumnInfo(name = "is_archive")
    val isArchive: Boolean,
)

@Entity
data class HabitWithHistoryEntity(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val historyEntries: List<HabitHistoryEntryEntity>
)

data class HabitWithOneDayHistoryEntryEntity(
    @Embedded val habit: HabitEntity,
    @Embedded val historyEntry: HabitHistoryEntryEntity?
)