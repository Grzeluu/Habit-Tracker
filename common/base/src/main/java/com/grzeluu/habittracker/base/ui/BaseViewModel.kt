package com.grzeluu.habittracker.base.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.domain.error.Error
import com.grzeluu.habittracker.base.domain.Result
import com.grzeluu.habittracker.base.domain.RootError
import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.ui.state.LoadingState
import com.grzeluu.habittracker.base.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<DATA> : ViewModel() {

    protected val loadingState: LoadingState = LoadingState()

    private val _isLoading = MutableStateFlow(false)
    private val isLoading: StateFlow<Boolean> = _isLoading

    private val errorChannel = Channel<Error>()
    private val error: Flow<Error?> = errorChannel.receiveAsFlow()

    protected abstract val uiDataState: StateFlow<DATA?>
    val uiState: StateFlow<UiState<DATA>> by lazy {
        combine(
            uiDataState,
            isLoading,
            error.onStart { emit(null) }
        ) { uiDataState, isLoading, error ->
            when {
                error != null -> UiState.Failure(error)
                isLoading || uiDataState == null -> UiState.Loading
                else -> UiState.Success(uiDataState)
            }
        }.catch { e ->
            Log.e("BaseViewModel", "Error while loading data", e)
            UiState.Failure(BaseError.GENERIC_ERROR)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState.Loading
        )
    }

    init {
        observeLoadingState(CoroutineScope(Dispatchers.IO))
    }

    protected suspend fun <D, E : RootError> Result<D, E>.handleResult(action: (D) -> Unit) {
        when (this) {
            is Result.Error -> {
                errorChannel.send(this.error)
            }

            is Result.Success -> {
                return action(this.data)
            }
        }
    }

    protected fun <D, E : RootError> handleResultFlow(resultFlow: Flow<Result<D, E>>): Flow<D?> {
        return resultFlow.map { result ->
            when (result) {
                is Result.Error -> {
                    errorChannel.send(result.error)
                    null
                }

                is Result.Success -> result.data
            }
        }
    }

    private fun observeLoadingState(scope: CoroutineScope) {
        loadingState.isInProgress
            .onEach { isLoading -> _isLoading.emit(isLoading) }
            .catch {
                _isLoading.emit(false)
            }.launchIn(scope);
    }
}