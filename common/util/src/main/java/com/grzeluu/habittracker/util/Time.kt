package com.grzeluu.habittracker.util

fun Int.toPresentationTime(
): String {

    val minString = "min"
    val hoursString = "h"

    return if (this < 60) {
        "$this $minString"
    } else {
        val hours = this / 60
        val minutes = this % 60
        "$hours $hoursString ${if (minutes > 0) "$minutes $minString" else ""}"
    }
}
