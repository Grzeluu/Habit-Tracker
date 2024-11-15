package com.grzeluu.habittracker.source.database.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class Converters {
    @TypeConverter
    fun fromStringList(strings: List<String>?): String? {
        return strings?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
        return data?.split(",")?.map { it.trim() }
    }

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

    @TypeConverter
    fun toTime(timeString: String?): LocalTime? {
        return timeString?.let {
            try {
                LocalTime.parse(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toTimeString(time: LocalTime?): String? {
        return time?.toString()
    }
}