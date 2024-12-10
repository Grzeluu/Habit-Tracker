package com.grzeluu.habittracker.util.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDate.format(format: String, timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return LocalDateTime(this.year, this.month, this.dayOfMonth, 0, 0).toInstant(timeZone)
        .toDateTimeString(DateTimeFormatter.ofPattern(format), timeZone)
}

private fun Instant.toDateTimeString(formatter: DateTimeFormatter, timeZone: TimeZone): String {
    val localDatetime = toLocalDateTime(timeZone)
    return formatter.format(localDatetime.toJavaLocalDateTime())
}
