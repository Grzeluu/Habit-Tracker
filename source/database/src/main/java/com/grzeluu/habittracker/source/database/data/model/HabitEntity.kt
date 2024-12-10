package com.grzeluu.habittracker.source.database.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
    val desirableDays: List<String>,
    @ColumnInfo(name = "is_notifications_enabled")
    val isNotificationEnabled: Boolean,
    @ColumnInfo(name = "notification_time")
    val notificationTime: LocalTime?,
    @ColumnInfo(name = "effort_unit")
    val effortUnit: String,
    @ColumnInfo(name = "desired_effort_value")
    val desiredEffortValue: Float,
    @ColumnInfo(name = "addition_date")
    val additionDate: LocalDate,
    @ColumnInfo(name = "is_archive")
    val isArchive: Boolean,
)

data class HabitWithHistoryDbModel(
    @Embedded val habit: HabitEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "habit_id"
    )
    val historyEntries: List<HabitHistoryEntryEntity>
)

data class HabitWithOneDayHistoryEntryDbModel(
    @Embedded val habit: HabitEntity,
    @Embedded val historyEntry: HabitHistoryEntryEntity?
)