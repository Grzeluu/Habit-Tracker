package com.grzeluu.habittracker.util.numbers

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun Float.formatFloat(): String {
    return if (this % 1 == 0f) {
        this.toInt().toString()
    } else {
        String.format("%.2f", this).replace(Regex("0*$"), "").replace(Regex("\\.$"), "")

    }
}