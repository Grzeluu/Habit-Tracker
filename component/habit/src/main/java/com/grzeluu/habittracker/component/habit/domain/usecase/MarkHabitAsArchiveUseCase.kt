package com.grzeluu.habittracker.component.habit.domain.usecase


import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import timber.log.Timber
import javax.inject.Inject


class MarkHabitAsArchiveUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : UseCase<MarkHabitAsArchiveUseCase.Request, Unit, BaseError>() {

    data class Request(
        val habitId: Long,
        val isArchived: Boolean
    )

    override suspend fun execute(params: Request): Result<Unit, BaseError> {
        return try {
            habitRepository.markHabitAsArchived(params.habitId, params.isArchived)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}