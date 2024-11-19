package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import timber.log.Timber
import javax.inject.Inject


class SaveHabitHistoryEntryUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) : UseCase<SaveHabitHistoryEntryUseCase.Request, Unit, BaseError>() {

    data class Request(
        val habitId: Long,
        val historyEntry: HabitHistoryEntry
    )

    override suspend fun execute(params: Request): Result<Unit, BaseError> {
        return try {
            habitRepository.addHabitHistoryEntry(params.habitId, params.historyEntry)
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}