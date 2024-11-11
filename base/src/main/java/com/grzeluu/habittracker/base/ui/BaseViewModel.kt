package com.grzeluu.habittracker.base.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.domain.error.Error
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.domain.result.RootError
import com.grzeluu.habittracker.base.ui.state.LoadingState
import com.grzeluu.habittracker.base.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

abstract class BaseViewModel<DATA> : ViewModel() {

    protected val loadingState: LoadingState = LoadingState()

    protected val errorChannel = Channel<Error>()
    private val error: Flow<Error?> = errorChannel.receiveAsFlow()

    protected abstract val uiDataState: StateFlow<DATA?>
    val uiState: StateFlow<UiState<DATA>> by lazy {
        combine(
            uiDataState,
            loadingState.isInProgress,
            error.onStart { emit(null) }
        ) { uiDataState, isLoading, error ->
            when {
                error != null -> UiState.Failure(error)
                isLoading || uiDataState == null -> UiState.Loading
                else -> UiState.Success(uiDataState)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = UiState.Loading
            )
    }


    protected suspend fun <D, E : RootError> Result<D, E>.handleResult(action: suspend (D) -> Unit = {}) {
        when (this) {
            is Result.Error -> {
                errorChannel.send(this.error)
            }

            is Result.Success -> {
                return action(this.data)
            }
        }
    }

    protected suspend fun <D, E : RootError> Flow<Result<D, E>>.collectLatestResult(function: suspend (D) -> Unit) {
        this.map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> {
                    errorChannel.send(result.error)
                    null
                }
            }
        }.filterNotNull().collectLatest {
            function(it.data)
        }
    }
}