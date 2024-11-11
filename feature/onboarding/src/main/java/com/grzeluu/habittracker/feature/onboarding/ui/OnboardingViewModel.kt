package com.grzeluu.habittracker.feature.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.settings.domain.usecase.GetSettingsUseCase
import com.grzeluu.habittracker.component.settings.domain.usecase.SaveSettingsUseCase
import com.grzeluu.habittracker.feature.onboarding.ui.event.NavigationEvent
import com.grzeluu.habittracker.feature.onboarding.ui.state.OnboardingStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : BaseViewModel<OnboardingStateData>() {

    private val navigationChannel = Channel<NavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    private var _isDarkModeEnabled = MutableStateFlow<Boolean?>(null)
    private val isDarkModeEnabled: StateFlow<Boolean?> = _isDarkModeEnabled

    private var _isNotificationsEnabled = MutableStateFlow(false)
    private val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled

    override val uiDataState: StateFlow<OnboardingStateData?>
        get() = combine(
            isDarkModeEnabled,
            isNotificationsEnabled
        ) { isDarkModeEnabled, isNotificationsEnabled ->
            OnboardingStateData(
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
            getSettingsUseCase(Unit).collectLatestResult { data ->
                _isDarkModeEnabled.emit(data.isDarkModeEnabled)
                _isNotificationsEnabled.emit(data.isNotificationsEnabled)
            }
        }
    }

    fun changeNotificationSettings(isEnabled: Boolean) {
        _isNotificationsEnabled.update { isEnabled }
    }

    fun changeDarkModeSettings(isDarkModeEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSettings(isDarkModeEnabled, isNotificationsEnabled.value)
        }
    }

    fun saveSettingsAndNavigate(destinationNavigationEvent: NavigationEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            saveSettings(isDarkModeEnabled.value, isNotificationsEnabled.value)
            withContext(Dispatchers.Main) {
                navigationChannel.send(destinationNavigationEvent)
            }
            loadingState.decrementTasksInProgress()
        }
    }

    private suspend fun OnboardingViewModel.saveSettings(
        isDarkModeEnabled: Boolean?,
        isNotificationsEnabled: Boolean
    ) {
        saveSettingsUseCase(
            SaveSettingsUseCase.Request(
                isDarkModeEnabled = isDarkModeEnabled,
                isNotificationsEnabled = isNotificationsEnabled
            )
        ).handleResult()
    }
}