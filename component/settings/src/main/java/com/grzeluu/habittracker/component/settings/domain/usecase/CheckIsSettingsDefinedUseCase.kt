package com.grzeluu.habittracker.component.settings.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class CheckIsSettingsDefinedUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<Unit, Boolean, BaseError>() {
    override suspend fun execute(params: Unit): Result<Boolean, BaseError> {
        return try {
            Result.Success(
                settingsRepository.getSettings().firstOrNull() != null
            )
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}
