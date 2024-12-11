package com.grzeluu.habittracker.feature.settings.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import com.grzeluu.habittracker.component.settings.domain.usecase.SaveSettingsUseCase
import com.grzeluu.habittracker.feature.settings.event.SettingsEvent
import com.grzeluu.habittracker.feature.settings.state.SettingsStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveSettings: SaveSettingsUseCase,
    private val getSettings: GetSettingsUseCase
) : BaseViewModel<SettingsStateData>() {

    private var _isDarkModeEnabled = MutableStateFlow<Boolean?>(null)
    private val isDarkModeEnabled: StateFlow<Boolean?> = _isDarkModeEnabled

    private var _isNotificationsEnabled = MutableStateFlow(false)
    private val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled

    override val uiDataState: StateFlow<SettingsStateData?>
        get() = combine(
            isDarkModeEnabled,
            isNotificationsEnabled
        ) { isDarkModeEnabled, isNotificationsEnabled ->
            SettingsStateData(
                isDarkModeEnabled = isDarkModeEnabled,
                isNotificationsEnabled = isNotificationsEnabled
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
            getSettings(Unit).collectLatestResult { data ->
                _isDarkModeEnabled.emit(data.isDarkModeEnabled)
                _isNotificationsEnabled.emit(data.isNotificationsEnabled)
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnChangeDarkMode -> changeDarkModeSettings(event)
            is SettingsEvent.OnChangeNotifications -> changeNotificationSettings(event)
        }
    }

    private fun changeNotificationSettings(event: SettingsEvent.OnChangeNotifications) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettings.invoke(
                SaveSettingsUseCase.Request(
                    isDarkModeEnabled = isDarkModeEnabled.value,
                    isNotificationsEnabled = event.isEnabled
                )
            )
        }
    }

    private fun changeDarkModeSettings(event: SettingsEvent.OnChangeDarkMode) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettings.invoke(
                SaveSettingsUseCase.Request(
                    isDarkModeEnabled = event.isDarkModeEnabled,
                    isNotificationsEnabled = isNotificationsEnabled.value
                )
            )
        }
    }

}