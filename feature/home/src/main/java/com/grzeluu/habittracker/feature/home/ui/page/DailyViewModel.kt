package com.grzeluu.habittracker.feature.home.ui.page

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.UiStateViewModel
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.usecase.GetDailyHabitInfosUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.SaveHabitHistoryEntryUseCase
import com.grzeluu.habittracker.feature.home.ui.event.DailyEvent
import com.grzeluu.habittracker.feature.home.ui.state.DailyDataState
import com.grzeluu.habittracker.feature.home.ui.state.DailyStatisticsData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(assistedFactory = DailyViewModel.DailyViewModelFactory::class)
class DailyViewModel @AssistedInject constructor(
    @Assisted val date: LocalDate,
    private val getDailyHabitInfosUseCase: GetDailyHabitInfosUseCase,
    private val saveHabitHistoryEntryUseCase: SaveHabitHistoryEntryUseCase
) : UiStateViewModel<DailyDataState>() {

    @AssistedFactory
    interface DailyViewModelFactory {
        fun create(date: LocalDate): DailyViewModel
    }

    private val _dailyHabits = MutableStateFlow<List<DailyHabitInfo>?>(null)
    private val dailyHabits: StateFlow<List<DailyHabitInfo>?> = _dailyHabits.asStateFlow()

    private val dailyStatisticsData = dailyHabits.filterNotNull().mapLatest {
        DailyStatisticsData(
            totalHabits = it.size,
            totalProgress = it.map { habit -> habit.effortProgress }.sum(),
            habitsDone = it.count { habit -> habit.isDone },
        )
    }

    override val uiDataState: StateFlow<DailyDataState?>
        get() = combine(
            dailyHabits.filterNotNull(),
            dailyStatisticsData,
        ) { dailyHabits, dailyStatisticsData ->
            DailyDataState(
                dailyHabits = dailyHabits,
                dailyStatistics = dailyStatisticsData,
            )
        }.onStart {
            getHabitsData()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: DailyEvent) {
        when (event) {
            is DailyEvent.OnSaveDailyEffort -> {
                handleSaveDailyEffortEvent(event)
            }
        }
    }

    private fun handleSaveDailyEffortEvent(event: DailyEvent.OnSaveDailyEffort) {
        viewModelScope.launch(Dispatchers.IO) {
            saveHabitHistoryEntryUseCase.invoke(
                SaveHabitHistoryEntryUseCase.Request(
                    habitId = event.habitId,
                    historyEntry = HabitHistoryEntry(
                        date = date,
                        currentEffort = event.effort
                    )
                )
            ).handleResult()
        }
    }

    private fun getHabitsData() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            getDailyHabitInfosUseCase.invoke(
                GetDailyHabitInfosUseCase.Request(date = date)
            ).collectLatestResult {
                loadingState.decrementTasksInProgress()
                _dailyHabits.value = it
            }
        }
    }
}