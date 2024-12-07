package com.grzeluu.habittracker.common.ui.state

import com.grzeluu.habittracker.common.ui.text.UiText

data class FieldState<T>(
    val value: T,
    val errorMassage: UiText? = null
)