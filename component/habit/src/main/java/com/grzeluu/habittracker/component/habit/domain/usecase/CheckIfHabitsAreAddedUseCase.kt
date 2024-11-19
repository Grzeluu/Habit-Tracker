package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.FlowUseCase
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class CheckIfHabitsAreAddedUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : FlowUseCase<Unit, Boolean, BaseError>() {

    override fun execute(params: Unit): Flow<Result<Boolean, BaseError>> = flow {
        emitAll(habitRepository.getHabitsCount().map { count ->
            Result.Success(count > 0)
        }.catch { e ->
            Timber.e(e)
            emit(Result.Error(BaseError.READ_ERROR))
        })
    }
}