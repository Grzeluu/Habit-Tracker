package com.grzeluu.habittracker.source.database.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "habits_notifications",
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class HabitNotificationEntity(
    @PrimaryKey
    @ColumnInfo("habit_id")
    val habitId: Long = 0,

    @ColumnInfo("date_time")
    val dateTime: LocalDateTime,
)

data class HabitNotificationWithHabitEntity(
    @Embedded val habit: HabitWithHistoryDbModel,
    @Embedded val notification: HabitNotificationEntity
)
