package com.grzeluu.habittracker.util.datetime

import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun LocalTime.format(): String {
    val localizedTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    return this.toJavaLocalTime().format(localizedTimeFormatter) + ':' + this.toJavaLocalTime()
        .format(localizedTimeFormatter)
}
