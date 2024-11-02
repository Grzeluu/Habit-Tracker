package com.grzeluu.habittracker.base.domain.usecase

import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.result.RootError

abstract class UseCase<PARAMS, DATA, ERROR : RootError> {

    suspend operator fun invoke(params: PARAMS): Result<DATA, ERROR> = execute(params)

    abstract suspend fun execute(params: PARAMS): Result<DATA, ERROR>
}