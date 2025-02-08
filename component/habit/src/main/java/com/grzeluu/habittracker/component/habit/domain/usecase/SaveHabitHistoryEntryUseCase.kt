package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationScheduler
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject


class SaveHabitHistoryEntryUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationScheduler: NotificationScheduler
) : UseCase<SaveHabitHistoryEntryUseCase.Request, Unit, BaseError>() {

    data class Request(
        val habitId: Long,
        val historyEntry: HabitHistoryEntry
    )

    override suspend fun execute(params: Request): Result<Unit, BaseError> {
        return try {
            val habit = habitRepository.getHabit(params.habitId).firstOrNull()
                ?: return Result.Error(BaseError.READ_ERROR)
            habitRepository.addHabitHistoryEntry(params.habitId, params.historyEntry)

            if (habit.notification is HabitNotificationSetting.Enabled
                && params.historyEntry.currentEffort >= habit.effort.desiredValue
            ) {
                val notification = notificationScheduler.calculateNextNotificationForHabit(habit)
                notificationRepository.addOrUpdateHabitNotification(notification)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}