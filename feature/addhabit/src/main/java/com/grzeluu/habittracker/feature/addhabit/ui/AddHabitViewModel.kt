package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.domain.error.BaseError
import com.grzeluu.habittracker.base.domain.result.Result
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.common.ui.state.FieldState
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.component.habit.domain.error.HabitValidationError
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotification
import com.grzeluu.habittracker.component.habit.domain.usecase.AddOrUpdateHabitUseCase
import com.grzeluu.habittracker.component.habit.domain.usecase.GetHabitUseCase
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitNavigationEvent
import com.grzeluu.habittracker.feature.addhabit.ui.mapper.asUiText
import com.grzeluu.habittracker.feature.addhabit.ui.navigation.AddHabitArgument
import com.grzeluu.habittracker.feature.addhabit.ui.state.AddHabitDataState
import com.grzeluu.habittracker.feature.addhabit.ui.state.NotificationSettings
import com.grzeluu.habittracker.util.datetime.getCurrentDate
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalTime
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

    private val _nameValidationError = MutableStateFlow<UiText?>(null)
    private val nameValidationError: StateFlow<UiText?> = _nameValidationError.asStateFlow()

    private val nameFieldState = combine(
        name, nameValidationError
    ) { name, nameValidationError -> FieldState(name, nameValidationError) }

    private var _description = MutableStateFlow<String?>(null)
    private val description: StateFlow<String?> = _description

    private var _color = MutableStateFlow(CardColor.entries.random())
    private val color: StateFlow<CardColor> = _color

    private var _icon = MutableStateFlow(CardIcon.entries.random())
    private val icon: StateFlow<CardIcon> = _icon

    private var _selectedDays = MutableStateFlow<List<Day>>(emptyList())
    private val selectedDays: StateFlow<List<Day>> = _selectedDays

    private val _selectedDaysValidationError = MutableStateFlow<UiText?>(null)
    private val selectedDaysValidationError: StateFlow<UiText?> = _selectedDaysValidationError.asStateFlow()

    private val selectedDaysFieldState = combine(
        selectedDays, selectedDaysValidationError
    ) { selectedDays, selectedDaysValidationError -> FieldState(selectedDays, selectedDaysValidationError) }

    private var _dailyEffort = MutableStateFlow<String>("1")
    private val dailyEffort: StateFlow<String?> = _dailyEffort

    private val _effortUnit = MutableStateFlow(EffortUnit.REPEAT)
    private val effortUnit: StateFlow<EffortUnit> = _effortUnit

    private var _isNotificationsEnabled = MutableStateFlow(false)
    private val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled

    private var _notificationTime = MutableStateFlow(LocalTime(17, 0))
    private val notificationTime: StateFlow<LocalTime> = _notificationTime

    private val notificationSettings = combine(
        isNotificationsEnabled,
        notificationTime
    ) { isNotificationsEnabled, notificationTime ->
        NotificationSettings(
            isEnabled = isNotificationsEnabled,
            time = notificationTime
        )
    }

    override val uiDataState: StateFlow<AddHabitDataState?>
        get() = combine(
            nameFieldState,
            description,
            color,
            icon,
            selectedDaysFieldState,
            dailyEffort,
            effortUnit,
            notificationSettings
        ) { nameFieldState, description, color, icon, selectedDaysFieldState, dailyEffort, effortUnit, notificationSettings ->
            AddHabitDataState(
                nameField = nameFieldState,
                description = description,
                color = color,
                icon = icon,
                selectedDaysField = selectedDaysFieldState,
                dailyEffort = dailyEffort,
                effortUnit = effortUnit,
                notificationSettings = notificationSettings
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
                _selectedDays.value = if (selectedDays.value.containsAll(allDays)) emptyList() else allDays
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
                _selectedDaysValidationError.value = null
            }

            is AddHabitEvent.OnDescriptionChanged -> {
                _description.value = event.value
            }

            is AddHabitEvent.OnIconChanged -> {
                _icon.value = event.icon
            }

            is AddHabitEvent.OnNameChanged -> {
                _name.value = event.value
                _nameValidationError.value = null
            }

            is AddHabitEvent.OnNotificationsEnabledChanged -> {
                _isNotificationsEnabled.value = event.value
            }

            is AddHabitEvent.OnNotificationTimeChanged -> {
                _notificationTime.value =  event.time
            }

            AddHabitEvent.AddHabit -> {
                addHabit()
            }
        }
    }

    private fun addHabit() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingState.incrementTasksInProgress()
            val result = addOrUpdateHabitUseCase.invoke(
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
                    additionDate = getCurrentDate(),
                    habitNotification = HabitNotification.Disabled
                )
            )
            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        HabitValidationError.EMPTY_DAYS -> _selectedDaysValidationError.emit(result.error.asUiText())
                        HabitValidationError.EMPTY_NAME -> _nameValidationError.emit(result.error.asUiText())
                        else -> errorChannel.send(result.error)
                    }
                }

                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        navigationChannel.send(AddHabitNavigationEvent.NAVIGATE_AFTER_SAVE)
                    }
                }
            }

            loadingState.decrementTasksInProgress()
        }
    }
}
