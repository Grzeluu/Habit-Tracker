package com.grzeluu.habittracker.base.ui.state

import androidx.annotation.StringRes

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val exception: Throwable, @StringRes val stringMessageId: Int? = null) : UiState<Nothing>()

    val <T> UiState<T>.data: T?
        get() = (this as? Success)?.data;
}

