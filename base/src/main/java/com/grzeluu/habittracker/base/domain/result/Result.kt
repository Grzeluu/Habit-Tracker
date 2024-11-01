package com.grzeluu.habittracker.base.domain.result

import com.grzeluu.habittracker.base.domain.error.Error

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D, out E : RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E : RootError>(val error: RootError) : Result<D, E>
}