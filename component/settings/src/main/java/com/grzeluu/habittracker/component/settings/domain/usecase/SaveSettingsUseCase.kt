package com.grzeluu.habittracker.component.settings.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.UseCase
import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.component.settings.domain.repository.SettingsRepository
import timber.log.Timber
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCase<SaveSettingsUseCase.Request, Unit, BaseError>() {

    data class Request(
        val isDarkModeEnabled: Boolean?,
        val isNotificationsEnabled: Boolean
    )

    override suspend fun execute(params: Request): Result<Unit, BaseError> {
        return try {
            Result.Success(
                settingsRepository.saveSettings(
                    Settings(
                        isDarkModeEnabled = params.isDarkModeEnabled,
                        isNotificationsEnabled = params.isNotificationsEnabled
                    )
                )
            )
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(BaseError.SAVE_ERROR)
        }
    }
}