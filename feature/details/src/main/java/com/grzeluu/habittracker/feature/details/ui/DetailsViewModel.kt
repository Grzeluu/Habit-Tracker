package com.grzeluu.habittracker.feature.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.component.habit.domain.usecase.MarkHabitAsArchiveUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.DeleteHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.SaveHabitHistoryEntryUseCase
import com.grzeluu.habittracker.feature.details.ui.enum.ProgressPeriod
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getHabitUseCase: GetHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val markHabitAsArchiveUseCase: MarkHabitAsArchiveUseCase,
    private val saveHabitHistoryEntryUseCase: SaveHabitHistoryEntryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailsDataState>() {

    val habitId = requireNotNull(savedStateHandle.get<Long>(DetailsArguments.HABIT_ID))

    private val navigationChannel = Channel<DetailsNavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    private val _habit = MutableStateFlow<Habit?>(null)
    private val habit: StateFlow<Habit?> = _habit.asStateFlow()

    private val _lastDays = MutableStateFlow<List<LocalDate>?>(null)
    private val lastDays: StateFlow<List<LocalDate>?> = _lastDays.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(ProgressPeriod.WEEK)
    private val selectedPeriod: StateFlow<ProgressPeriod> = _selectedPeriod.asStateFlow()

    private val periodStats = combine(
        habit.filterNotNull(),
        selectedPeriod.filterNotNull()
    ) { habit, period ->
        updatePeriodStats(habit, period)
    }.flowOn(Dispatchers.Default)

    override val uiDataState: StateFlow<DetailsDataState?>
        get() = combine(
            habit.filterNotNull(),
            lastDays.filterNotNull(),
            selectedPeriod,
            periodStats.filterNotNull()
        ) { habit, daysOfWeek, selectedPeriod, periodStats ->
            DetailsDataState(
                habit = habit,
                lastDays = daysOfWeek,
                selectedPeriod = selectedPeriod,
                periodStats = periodStats
            )
        }.onStart {
            getHabit()
            initDays()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.OnArchiveHabit -> markHabitAsArchive(true)
            DetailsEvent.OnUnarchiveHabit -> markHabitAsArchive(false)
            DetailsEvent.OnDeleteHabit -> deleteHabit()
            is DetailsEvent.OnSaveDailyEffort -> saveDailyEffort(event)
            is DetailsEvent.OnSelectPeriod -> selectPeriod(event)
        }
    }

    private fun selectPeriod(event: DetailsEvent.OnSelectPeriod) {
        _selectedPeriod.update { event.period }
    }

    private fun updatePeriodStats(habit: Habit, period: ProgressPeriod): List<Pair<Float, String>> {
        val dateFrom = getDateByPeriod(period)
        val stats = habit.getDailyEffortEntries(dateFrom).map {
            val dateString = when (period) {
                ProgressPeriod.WEEK -> it.date.dayOfWeek.name[0].toString()
                ProgressPeriod.MONTH -> it.date.dayOfMonth.toString()
                ProgressPeriod.YEAR -> it.date.month.toString()[0].toString()
            }
            Pair(it.currentEffort, dateString)
        }
        return stats
    }

    private fun getDateByPeriod(period: ProgressPeriod): LocalDate {
        val today = getCurrentDate()
        return when (period) {
            ProgressPeriod.WEEK -> today.minus(7, DateTimeUnit.DAY)
            ProgressPeriod.MONTH -> today.minus(1, DateTimeUnit.MONTH)
            ProgressPeriod.YEAR -> today.minus(1, DateTimeUnit.YEAR)
        }
    }

    private fun saveDailyEffort(event: DetailsEvent.OnSaveDailyEffort) {
        viewModelScope.launch(Dispatchers.IO) {
            saveHabitHistoryEntryUseCase.invoke(
                SaveHabitHistoryEntryUseCase.Request(
                    habitId = habitId,
                    historyEntry = HabitHistoryEntry(
                        date = event.date,
                        currentEffort = event.effort
                    )
                )
            ).handleResult()
        }
    }

    private fun deleteHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            habit.value?.let { deleteHabitUseCase(it) }
            navigationChannel.send(DetailsNavigationEvent.NAVIGATE_BACK)
        }
    }

    private fun markHabitAsArchive(isArchive: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            habit.value?.let {
                markHabitAsArchiveUseCase(
                    MarkHabitAsArchiveUseCase.Request(
                        habitId = it.id,
                        isArchived = isArchive
                    )
                )
                if (isArchive) {
                    navigationChannel.send(DetailsNavigationEvent.NAVIGATE_BACK)
                }
            }
        }
    }

    private fun initDays() {
        viewModelScope.launch(Dispatchers.Default) {
            loadingState.incrementTasksInProgress()
            val today = getCurrentDate()
            val days = mutableListOf<LocalDate>()

            for (i in 0..6) {
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
            }
        }
    }
}