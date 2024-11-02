package com.grzeluu.habittracker.base.domain.usecase

import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.result.RootError
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<PARAMS, DATA, ERROR : RootError> {
    operator fun invoke(params: PARAMS): Flow<Result<DATA, ERROR>> = execute(params)

    protected abstract fun execute(params: PARAMS): Flow<Result<DATA, ERROR>>
}