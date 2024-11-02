package com.grzeluu.habittracker.activity

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.activity.state.MainActivityStateData
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.settings.domain.model.Settings
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase
) : BaseViewModel<MainActivityStateData>() {

    private var _settings = MutableStateFlow<Settings?>(null)
    private val settings: StateFlow<Settings?> = _settings

    override val uiDataState: StateFlow<MainActivityStateData?> =
        settings.map {
            MainActivityStateData(
                settings = it
            )
        }.onStart {
            getSettings()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

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