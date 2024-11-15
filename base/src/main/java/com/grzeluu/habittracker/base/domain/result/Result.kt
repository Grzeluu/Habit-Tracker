package com.grzeluu.habittracker.base.domain.result

import com.grzeluu.habittracker.base.domain.error.Error

typealias RootError = Error

sealed interface Result<out D, out E : RootError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : RootError>(val error: RootError) : Result<Nothing, E>
}