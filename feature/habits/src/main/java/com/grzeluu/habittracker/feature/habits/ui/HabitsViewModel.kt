package com.grzeluu.habittracker.feature.habits.ui

import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.usecase.GetDailyHabitInfosUseCase
import com.grzeluu.habittracker.feature.habits.ui.event.HabitsEvent
import com.grzeluu.habittracker.feature.habits.ui.state.DailyStatisticsData
import com.grzeluu.habittracker.feature.habits.ui.state.HabitsDataState
import com.grzeluu.habittracker.util.date.getCurrentDate
import com.grzeluu.habittracker.util.enums.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val getDailyHabitInfosUseCase: GetDailyHabitInfosUseCase
) : BaseViewModel<HabitsDataState>() {

    private val _daysOfWeek = MutableStateFlow<List<Pair<Day, LocalDate>>>(emptyList())
    private val daysOfWeek: StateFlow<List<Pair<Day, LocalDate>>> = _daysOfWeek.asStateFlow()

    private val _selectedDay = MutableStateFlow(getCurrentDate())
    private val selectedDay: StateFlow<LocalDate> = _selectedDay.asStateFlow()

    private val _dailyHabits = MutableStateFlow<List<DailyHabitInfo>>(emptyList())
    private val dailyHabits: StateFlow<List<DailyHabitInfo>> = _dailyHabits.asStateFlow()

    private val dailyStatisticsData = dailyHabits.mapLatest {
        DailyStatisticsData(
            totalHabits = it.size,
            totalProgress = it.map { habit -> habit.effortProgress }.sum(),
            habitsDone = it.count { habit -> habit.isDone },
        )
    }

    override val uiDataState: StateFlow<HabitsDataState?>
        get() = combine(
            daysOfWeek,
            selectedDay,
            dailyHabits,
            dailyStatisticsData,
        ) { daysOfWeek, selectedDay, dailyHabits, dailyStatisticsData ->
            HabitsDataState(
                daysOfWeek = daysOfWeek,
                selectedDay = selectedDay,
                dailyHabits = dailyHabits,
                dailyStatistics = dailyStatisticsData,
            )
        }.onStart {
            getHabitsData()
            initDays()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: HabitsEvent) {
        when(event) {
            is HabitsEvent.OnChangeSelectedDay -> {
                _selectedDay.value = event.dateTime
            }
        }
    }

    private fun getHabitsData() {
        viewModelScope.launch {
            selectedDay.flatMapLatest { date ->
                getDailyHabitInfosUseCase.invoke(
                    GetDailyHabitInfosUseCase.Request(dateTime = date)
                )
            }.collectLatestResult { habitInfos ->
                _dailyHabits.emit(habitInfos)
            }
        }
    }

    private fun initDays() {
        loadingState.incrementTasksInProgress()
        val today = selectedDay.value
        val currentDayOfWeek = today.dayOfWeek.value
        val days = mutableListOf<Pair<Day, LocalDate>>()

        for (i in 0..6) {
            val dayDate = today.plus(i - (currentDayOfWeek - 1), DateTimeUnit.DAY)
            days.add(Pair(Day.get(dayDate.dayOfWeek.value), dayDate))
        }
        _daysOfWeek.value = days.toList()
        loadingState.decrementTasksInProgress()
    }
}