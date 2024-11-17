package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import timber.log.Timber
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : UseCase<Habit, Unit, BaseError>() {

    override suspend fun execute(params: Habit): Result<Unit, BaseError> {
        return try {
            habitRepository.addHabit(params)
            if (params.habitNotification is HabitNotification.Enabled) {
                //TODO initialize notifications
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}