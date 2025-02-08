package com.grzeluu.habittracker.component.habit.domain.usecase


import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.component.habit.domain.repository.HabitRepository
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import com.grzeluu.habittracker.component.habit.infrastructure.NotificationScheduler
import timber.log.Timber
import javax.inject.Inject


class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository,
    private val notificationRepository: NotificationRepository,
    private val notificationScheduler: NotificationScheduler,
) : UseCase<Habit, Unit, BaseError>() {

    override suspend fun execute(params: Habit): Result<Unit, BaseError> {
        return try {
            habitRepository.deleteHabit(params)
            if (params.notification is HabitNotificationSetting.Enabled) {
                val notification = notificationRepository.deleteHabitNotificationByHabitId(params.id)
                notification?.let { notificationScheduler.cancelNotification(it) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}