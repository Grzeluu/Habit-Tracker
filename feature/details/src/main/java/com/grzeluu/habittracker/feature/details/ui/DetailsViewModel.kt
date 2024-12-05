package com.grzeluu.habittracker.feature.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.usecase.ArchiveHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.DeleteHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitUseCase
import com.grzeluu.habittracker.feature.details.ui.event.DetailsEvent
import com.grzeluu.habittracker.feature.details.ui.event.DetailsNavigationEvent
import com.grzeluu.habittracker.feature.details.ui.navigation.DetailsArguments
import com.grzeluu.habittracker.feature.details.ui.state.DetailsDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getHabitUseCase: GetHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val archiveHabitUseCase: ArchiveHabitUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailsDataState>() {

    private val habitId = requireNotNull(savedStateHandle.get<Long>(DetailsArguments.HABIT_ID))

    private val navigationChannel = Channel<DetailsNavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    private val _habit = MutableStateFlow<Habit?>(null)
    private val habit: StateFlow<Habit?> = _habit.asStateFlow()

    override val uiDataState: StateFlow<DetailsDataState?>
        get() = habit.filterNotNull().mapNotNull {
            DetailsDataState(habit = it)
        }.onStart {
            getHabit()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.OnArchiveHabit -> archiveHabit()
            DetailsEvent.OnDeleteHabit -> deleteHabit()
        }
    }

    private fun deleteHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            habit.value?.let { deleteHabitUseCase(it) }
            navigationChannel.send(DetailsNavigationEvent.NAVIGATE_BACK)
        }
    }

    private fun archiveHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            habit.value?.let { archiveHabitUseCase(it) }
            navigationChannel.send(DetailsNavigationEvent.NAVIGATE_BACK)
        }
    }

    private fun getHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            getHabitUseCase(GetHabitUseCase.Request(habitId)).collectLatestResult { habit ->
                _habit.emit(habit)
            }
        }
    }
}