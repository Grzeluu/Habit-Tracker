package com.grzeluu.habittracker.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.state.LoadingState
import com.grzeluu.habittracker.base.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<DATA> : ViewModel() {

    private val loadingState: LoadingState = LoadingState()

    private val _isLoading = MutableStateFlow(false)
    private val isLoading: StateFlow<Boolean> = _isLoading

    protected abstract val _uiState: StateFlow<UiState<DATA>>
    val uiState: StateFlow<UiState<DATA>> by lazy {
        combine(
            _uiState,
            isLoading
        ) { uiState, isLoading ->
            when {
                uiState is UiState.Failure -> uiState
                isLoading -> UiState.Loading
                else -> uiState
            }
        }.catch { ex ->
            UiState.Failure(ex)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState.Loading
        )
    }

    init {
        observeLoadingState(CoroutineScope(Dispatchers.IO))
    }

    private fun observeLoadingState(scope: CoroutineScope) {
        loadingState.isInProgress
            .onEach { isLoading -> _isLoading.emit(isLoading) }
            .catch {
                _isLoading.emit(false)
            }.launchIn(scope);
    }
}