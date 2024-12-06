package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.usecase.AddOrUpdateHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitUseCase
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitNavigationEvent
import com.grzeluu.habittracker.feature.addhabit.ui.navigation.AddHabitArgument
import com.grzeluu.habittracker.feature.addhabit.ui.state.AddHabitDataState
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.flow.combine
import com.grzeluu.habittracker.util.numbers.formatFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val getHabitUseCase: GetHabitUseCase,
    private val addOrUpdateHabitUseCase: AddOrUpdateHabitUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AddHabitDataState>() {

    val habitId = savedStateHandle.get<Long?>(AddHabitArgument.HABIT_ID)

    private val navigationChannel = Channel<AddHabitNavigationEvent>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    private var _name = MutableStateFlow("")
    private val name: StateFlow<String> = _name

    private var _description = MutableStateFlow<String?>(null)
    private val description: StateFlow<String?> = _description

    private var _color = MutableStateFlow(CardColor.entries.random())
    private val color: StateFlow<CardColor> = _color

    private var _icon = MutableStateFlow(CardIcon.entries.random())
    private val icon: StateFlow<CardIcon> = _icon

    private var _selectedDays = MutableStateFlow<List<Day>>(emptyList())
    private val selectedDays: StateFlow<List<Day>> = _selectedDays

    private var _dailyEffort = MutableStateFlow<String>("1")
    private val dailyEffort: StateFlow<String?> = _dailyEffort

    private val _effortUnit = MutableStateFlow(EffortUnit.REPEAT)
    private val effortUnit: StateFlow<EffortUnit> = _effortUnit

    private var _isNotificationsEnabled = MutableStateFlow(false)
    private val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled

    override val uiDataState: StateFlow<AddHabitDataState?>
        get() = combine(
            name, description, color, icon, selectedDays, dailyEffort, effortUnit, isNotificationsEnabled
        ) { name, description, color, icon, selectedDays, dailyEffort, effortUnit, isNotificationsEnabled ->
            AddHabitDataState(
                name = name,
                description = description,
                color = color,
                icon = icon,
                selectedDays = selectedDays,
                dailyEffort = dailyEffort,
                effortUnit = effortUnit,
                isNotificationsEnabled = isNotificationsEnabled
            )
        }.onStart {
            getEditedHabitData()
        }.stateIn(
            scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = null
        )

    private fun getEditedHabitData() {
        viewModelScope.launch(Dispatchers.IO) {
            if (habitId != null) {
                when (val result = getHabitUseCase(GetHabitUseCase.Request(habitId)).firstOrNull()) {
                    is Result.Success -> result.data?.let { handleHabitData(it) }
                    else -> errorChannel.send(BaseError.READ_ERROR)
                }
            }
        }
    }

    private suspend fun handleHabitData(habit: Habit) {
        _name.emit(habit.name)
        _description.emit(habit.description)
        _color.emit(habit.color)
        _icon.emit(habit.icon)
        _selectedDays.emit(habit.desirableDays)
        _dailyEffort.emit(habit.effort.desiredValue.formatFloat())
        _effortUnit.emit(habit.effort.effortUnit)
        _isNotificationsEnabled.emit(habit.habitNotification is HabitNotification.Enabled)
    }

    fun onEvent(event: AddHabitEvent) {
        when (event) {
            AddHabitEvent.OnAllDaysToggled -> {
                val allDays = Day.entries
                _selectedDays.value = if (selectedDays.value.containsAll(allDays)) {
                    emptyList()
                } else {
                    allDays
                }
            }

            is AddHabitEvent.OnColorChanged -> {
                _color.value = event.value
            }

            is AddHabitEvent.OnDailyGoalTextChanged -> {
                _dailyEffort.value = event.value
            }

            is AddHabitEvent.OnDailyGoalUnitChanged -> {
                _effortUnit.value = event.unit
            }

            is AddHabitEvent.OnDayChanged -> {
                val currentDays = selectedDays.value.toMutableList()
                if (event.isChecked) currentDays.add(event.day)
                else currentDays.remove(event.day)
                _selectedDays.value = currentDays
            }

            is AddHabitEvent.OnDescriptionChanged -> {
                _description.value = event.value
            }

            is AddHabitEvent.OnIconChanged -> {
                _icon.value = event.icon
            }

            is AddHabitEvent.OnNameChanged -> {
                _name.value = event.value
            }

            is AddHabitEvent.OnNotificationsEnabledChanged -> {
                _isNotificationsEnabled.value = event.value
            }

            AddHabitEvent.AddHabit -> {
                addHabit()
            }
        }
    }

    private fun addHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            addOrUpdateHabitUseCase.invoke(
                Habit(
                    id = habitId ?: 0L,
                    name = name.value,
                    description = description.value,
                    color = color.value,
                    icon = icon.value,
                    desirableDays = selectedDays.value,
                    effort = HabitDesiredEffort(
                        desiredValue = dailyEffort.value?.toFloat() ?: 1f, effortUnit = effortUnit.value
                    ),
                    habitNotification = HabitNotification.Disabled
                )
            ).handleResult()
            withContext(Dispatchers.Main) {
                navigationChannel.send(AddHabitNavigationEvent.NAVIGATE_AFTER_SAVE)
            }
            loadingState.decrementTasksInProgress()
        }
    }
}