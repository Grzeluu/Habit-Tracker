package com.grzeluu.habittracker.feature.onboarding.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.domain.Result
import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.error.Error
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.base.ui.state.UiState
import com.grzeluu.habittracker.feature.onboarding.ui.state.OnboardingStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : BaseViewModel<OnboardingStateData>() {

    private var _isDarkModeEnabled = MutableStateFlow(false)
    private val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled

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
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
}