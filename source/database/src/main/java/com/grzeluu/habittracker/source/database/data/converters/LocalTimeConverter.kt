package com.grzeluu.habittracker.source.database.data.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalTime

class LocalTimeConverter {
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