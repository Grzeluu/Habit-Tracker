package com.grzeluu.habittracker.source.database.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toLocalDateTime(dateTimeMillis: Long?): LocalDateTime? {
        return dateTimeMillis?.let {
            val instant = Instant.fromEpochMilliseconds(dateTimeMillis)
            instant.toLocalDateTime(TimeZone.UTC)
        }
    }

    @TypeConverter
    fun toLocalDateTimeMillis(dateTime: LocalDateTime?): Long? {
        return dateTime?.let {
            it.toInstant(TimeZone.UTC).toEpochMilliseconds()
        }
    }
}