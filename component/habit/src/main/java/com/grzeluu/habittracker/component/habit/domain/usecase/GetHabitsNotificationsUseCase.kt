package com.grzeluu.habittracker.component.habit.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.FlowUseCase
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetHabitsNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) : FlowUseCase<Unit, List<HabitNotification>, BaseError>() {

    override fun execute(params: Unit): Flow<Result<List<HabitNotification>, BaseError>> = flow {
        emitAll(notificationRepository.getHabitsNotification().map { data ->
            Result.Success(data)
        }.catch { e ->
            Timber.e(e)
            emit(Result.Error(BaseError.READ_ERROR))
        })
    }
}