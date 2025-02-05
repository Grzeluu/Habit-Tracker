package com.grzeluu.habittracker.feature.home.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.UiStateViewModel
import com.grzeluu.habittracker.component.habit.domain.usecase.CheckIfHabitsAreAddedUseCase
import com.grzeluu.habittracker.feature.home.ui.event.HomeEvent
import com.grzeluu.habittracker.feature.home.ui.state.HomeDataState
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val checkIfHabitsAreAddedUseCase: CheckIfHabitsAreAddedUseCase
) : UiStateViewModel<HomeDataState>() {

    private val _areHabitsAdded = MutableStateFlow<Boolean?>(null)
    private val areHabitsAdded: StateFlow<Boolean?> = _areHabitsAdded.asStateFlow()

    private val _daysOfWeek = MutableStateFlow<List<LocalDate>?>(null)
    private val daysOfWeek: StateFlow<List<LocalDate>?> = _daysOfWeek.asStateFlow()

    private val _selectedDay = MutableStateFlow(getCurrentDate())
    private val selectedDay: StateFlow<LocalDate> = _selectedDay.asStateFlow()

    override val uiDataState: StateFlow<HomeDataState?>
        get() = combine(
            daysOfWeek.filterNotNull(),
            selectedDay,
            areHabitsAdded.filterNotNull()
        ) { daysOfWeek, selectedDay, areHabitsAdded ->
            HomeDataState(
                daysOfWeek = daysOfWeek,
                selectedDay = selectedDay,
                areHabitsAdded = areHabitsAdded
            )
        }.onStart {
            checkIfHabitsAreAdded()
            initDays()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnChangeSelectedDay -> {
                _selectedDay.value = event.dateTime
            }
        }
    }

    private fun checkIfHabitsAreAdded() {
        viewModelScope.launch(Dispatchers.IO) {
            checkIfHabitsAreAddedUseCase(Unit).collectLatestResult { result ->
                _areHabitsAdded.emit(result)
            }
        }
    }

    private fun initDays() {
        loadingState.incrementTasksInProgress()
        val today = selectedDay.value
        val currentDayOfWeek = today.dayOfWeek.value
        val days = mutableListOf<LocalDate>()

        for (i in 0..6) {
            val dayDate = today.plus(i - (currentDayOfWeek - 1), DateTimeUnit.DAY)
            days.add(dayDate)
        }
        _daysOfWeek.value = days.toList()
        loadingState.decrementTasksInProgress()
    }
}