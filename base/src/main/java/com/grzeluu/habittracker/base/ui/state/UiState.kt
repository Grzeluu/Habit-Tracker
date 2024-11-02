package com.grzeluu.habittracker.base.ui.state

import com.grzeluu.habittracker.base.domain.error.Error

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val error: Error) : UiState<Nothing>()

    val <T> UiState<T>.data: T?
        get() = (this as? Success)?.data;
}

