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
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getHabitUseCase: GetHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val archiveHabitUseCase: ArchiveHabitUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailsDataState>() {

    val habitId = requireNotNull(savedStateHandle.get<Long>(DetailsArguments.HABIT_ID))

    private val navigationChannel = Channel<DetailsNavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    private val _habit = MutableStateFlow<Habit?>(null)
    private val habit: StateFlow<Habit?> = _habit.asStateFlow()

    private val _lastDays = MutableStateFlow<List<LocalDate>?>(null)
    private val lastDays: StateFlow<List<LocalDate>?> = _lastDays.asStateFlow()

    override val uiDataState: StateFlow<DetailsDataState?>
        get() = combine(
            habit.filterNotNull(),
            lastDays.filterNotNull()
        ) { habit, daysOfWeek ->
            DetailsDataState(
                habit = habit,
                lastDays = daysOfWeek
            )
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

    private fun initDays() {
        viewModelScope.launch(Dispatchers.Default) {
            loadingState.incrementTasksInProgress()
            val today = getCurrentDate()
            val days = mutableListOf<LocalDate>()

            for (i in 0..5) {
                val dayDate = today.minus(i, DateTimeUnit.DAY)
                days.add(dayDate)
            }
            _lastDays.value = days.toList().sorted()
            loadingState.decrementTasksInProgress()
        }
    }

    private fun getHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            getHabitUseCase(GetHabitUseCase.Request(habitId)).collectLatestResult { habit ->
                _habit.emit(habit)
                loadingState.decrementTasksInProgress()

                initDays()
            }
        }
    }
}