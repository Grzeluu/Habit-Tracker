package com.grzeluu.habittracker.source.database.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "habit_history_entries",
    primaryKeys = ["habit_id", "date"],
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
class HabitHistoryEntryEntity(
    @ColumnInfo("habit_id")
    val habitId: Long = 0,
    @ColumnInfo("date")
    val date: LocalDate,
    @ColumnInfo("current_effort")
    val currentEffort: Float,
    @ColumnInfo("note")
    val note: String?,
)