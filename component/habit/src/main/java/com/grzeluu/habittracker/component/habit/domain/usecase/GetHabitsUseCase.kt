package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.FlowUseCase
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : FlowUseCase<Unit, List<Habit>, BaseError>() {
    override fun execute(params: Unit): Flow<Result<List<Habit>, BaseError>> = flow {
        emitAll(habitRepository.getHabits().map { data ->
            Result.Success(data)
        }.catch { e ->
            Timber.e(e)
            emit(Result.Error(BaseError.READ_ERROR))
        })
    }
}