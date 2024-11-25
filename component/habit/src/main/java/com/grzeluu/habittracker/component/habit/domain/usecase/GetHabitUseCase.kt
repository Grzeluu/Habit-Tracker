package com.grzeluu.habittracker.component.habit.domain.usecase


import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.FlowUseCase
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import timber.log.Timber
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : FlowUseCase<GetHabitUseCase.Request, Habit?, BaseError>() {

    data class Request(
        val habitId: Long
    )

    override fun execute(params: Request): Flow<Result<Habit?, BaseError>> = flow {
        emitAll(habitRepository.getHabit(
            habitId = params.habitId
        ).map { data ->
            Result.Success(data)
        }.catch { e ->
            Timber.e(e)
            emit(Result.Error(BaseError.READ_ERROR))
        })
    }
}