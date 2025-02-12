package com.grzeluu.habittracker.util.numbers

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Float?.formatFloat(): String {
    if (this == null) return ""
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        String.format("%.2f", this).replace(Regex("0*$"), "").replace(Regex("\\.$"), "")

    }
}

@SuppressLint("DefaultLocale")
fun Float?.shortFormatFloat(): String {
    if (this == null) return ""
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        String.format("%.1f", this).replace(Regex("0*$"), "").replace(Regex("\\.$"), "")

    }
}