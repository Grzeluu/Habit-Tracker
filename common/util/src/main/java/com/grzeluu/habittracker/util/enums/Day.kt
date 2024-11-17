package com.grzeluu.habittracker.util.enums

enum class Day(val value: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {
        fun get(value: Int) = entries.first { it.value == value }
    }
}