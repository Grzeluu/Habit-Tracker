package com.grzeluu.habittracker.component.habit.domain.usecase


import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.domain.scheduler.NotificationManager
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject


class MarkHabitAsArchiveUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationManager: NotificationManager,
    private val notificationScheduler: NotificationScheduler
) : UseCase<MarkHabitAsArchiveUseCase.Request, Unit, BaseError>() {

    data class Request(
        val habitId: Long,
        val isArchived: Boolean
    )

    override suspend fun execute(params: Request): Result<Unit, BaseError> {
        return try {
            val archivedHabit = habitRepository.getHabit(params.habitId).firstOrNull()
                ?: return Result.Error(BaseError.READ_ERROR)

            habitRepository.markHabitAsArchived(params.habitId, params.isArchived)

            if (archivedHabit.notification is HabitNotificationSetting.Enabled) {
                if (params.isArchived) {
                    val notification = notificationRepository.deleteHabitNotificationByHabitId(params.habitId)
                    notification?.let { notificationScheduler.cancelNotification(it) }
                } else {
                    val notification = notificationManager.calculateNextNotificationForHabit(archivedHabit)
                    notificationRepository.addOrUpdateHabitNotification(notification)
                    notificationScheduler.scheduleNotification(notification)
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}