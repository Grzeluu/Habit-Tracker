package com.grzeluu.habittracker.activity.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.activity.ui.state.MainActivityStateData
import com.grzeluu.habittracker.base.ui.UiStateViewModel
import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.component.settings.domain.usecase.CheckIsSettingsDefinedUseCase
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val checkIsSettingsDefinedUseCase: CheckIsSettingsDefinedUseCase,
) : UiStateViewModel<MainActivityStateData>() {

    private var _settings = MutableStateFlow<Settings?>(null)
    private val settings: StateFlow<Settings?> = _settings

    private var _isSettingsDefinedOnAppStart = MutableStateFlow<Boolean?>(null)
    private val isSettingsDefinedOnAppStart: StateFlow<Boolean?> = _isSettingsDefinedOnAppStart

    override val uiDataState: StateFlow<MainActivityStateData?> =
        combine(
            settings,
            isSettingsDefinedOnAppStart.filterNotNull()
        ) { settings, isSettingsDefinedOnAppStart ->
            MainActivityStateData(
                settings = settings,
                isSettingsDefinedOnAppStart = isSettingsDefinedOnAppStart
            )
        }.onStart {
            checkIsSettingsDefined()
            getSettings()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private fun checkIsSettingsDefined() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            checkIsSettingsDefinedUseCase(Unit).handleResult {
                _isSettingsDefinedOnAppStart.emit(it)
            }
            loadingState.decrementTasksInProgress()
        }
    }

    private fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            getSettingsUseCase(Unit).collectLatestResult { data ->
                _settings.emit(data)
                loadingState.decrementTasksInProgress()
            }
        }
    }
}