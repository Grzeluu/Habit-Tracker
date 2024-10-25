package com.grzeluu.habittracker.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.state.UiState
import com.grzeluu.habittracker.feature.onboarding.ui.state.OnboardingStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    val _uiState: StateFlow<UiState<OnboardingStateData>>
        get() = flowOf(UiState.Success(OnboardingStateData(false, false))).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState.Loading
        )

}