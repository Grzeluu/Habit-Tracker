package com.grzeluu.habittracker.source.database.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun toDate(dateMillis: Long?): LocalDate? {
        return dateMillis?.let {
            LocalDate.fromEpochDays((it / (24 * 60 * 60 * 1000)).toInt())
        }
    }

    @TypeConverter
    fun toDateLong(date: LocalDate?): Long? {
        return date?.toEpochDays()?.toLong()?.times(24 * 60 * 60 * 1000)
    }
}