package com.grzeluu.habittracker.component.settings.domain.usecase

import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.usecase.FlowUseCase
import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.component.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : FlowUseCase<Unit, Settings, BaseError>() {

    override fun execute(params: Unit): Flow<Result<Settings, BaseError>> = flow {
        emitAll(
            settingsRepository.getSettings().map { settings ->
                if (settings == null) Result.Success(Settings.DEFAULT)
                else Result.Success(settings)
            }.catch { e ->
                Timber.e(e)
                emit(Result.Error(BaseError.READ_ERROR))
            })
    }
}