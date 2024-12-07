package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.error.HabitValidationError
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import timber.log.Timber
import javax.inject.Inject

class AddOrUpdateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : UseCase<Habit, Unit, BaseError>() {

    override suspend fun execute(params: Habit): Result<Unit, BaseError> {
        return try {
            if (params.name.isEmpty()) return Result.Error(HabitValidationError.EMPTY_NAME)
            if (params.desirableDays.isEmpty()) return Result.Error(HabitValidationError.EMPTY_DAYS)

            habitRepository.addOrUpdateHabit(params)

            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}