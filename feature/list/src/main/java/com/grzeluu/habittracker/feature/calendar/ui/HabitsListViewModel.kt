package com.grzeluu.habittracker.feature.calendar.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitsUseCase
import com.grzeluu.habittracker.feature.calendar.ui.model.HabitsListDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsListViewModel @Inject constructor(
    private val getHabits: GetHabitsUseCase
) : BaseViewModel<HabitsListDataState>() {

    private val _activeHabits = MutableStateFlow<List<Habit>>(emptyList())
    private val activeHabits: StateFlow<List<Habit>> = _activeHabits.asStateFlow()

    private val _archivedHabits = MutableStateFlow<List<Habit>>(emptyList())
    private val archivedHabits: StateFlow<List<Habit>> = _archivedHabits.asStateFlow()

    override val uiDataState: StateFlow<HabitsListDataState?>
        get() = combine(
            activeHabits,
            archivedHabits
        ) { activeHabits, archivedHabits ->
            HabitsListDataState(
                activeHabits = activeHabits,
                archivedHabits = archivedHabits,
            )
        }.onStart {
            getHabits()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private fun getHabits() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            getHabits(Unit).collectLatestResult { data ->
                _activeHabits.emit(data.filter { !it.isArchive })
                _archivedHabits.emit(data.filter { it.isArchive })
                loadingState.decrementTasksInProgress()
            }
        }
    }
}