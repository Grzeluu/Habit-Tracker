package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.compose.ui.semantics.text
import androidx.core.text.color
import androidx.lifecycle.viewModelScope
import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitNavigationEvent
import com.grzeluu.habittracker.feature.addhabit.ui.state.AddHabitDataState
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.Day
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.flow.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.text.toFloatOrNull

@HiltViewModel
class AddHabitViewModel @Inject constructor() : BaseViewModel<AddHabitDataState>() {

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

    private var _dailyEffort = MutableStateFlow<Float?>(1f)
    private val dailyEffort: StateFlow<Float?> = _dailyEffort

    private val _effortUnit = MutableStateFlow(EffortUnit.TIMES)
    private val effortUnit: StateFlow<EffortUnit> = _effortUnit

    private var _isNotificationsEnabled = MutableStateFlow(false)
    private val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled

    override val uiDataState: StateFlow<AddHabitDataState?>
        get() = combine(
            name,
            description,
            color,
            icon,
            selectedDays,
            dailyEffort,
            effortUnit,
            isNotificationsEnabled
        ) { name,
            description,
            color,
            icon,
            selectedDays,
            dailyEffort,
            effortUnit,
            isNotificationsEnabled ->
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
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    fun onEvent(event: AddHabitEvent) {
        when (event) {
            AddHabitEvent.OnAllDaysToggled -> {
                val allDays = Day.entries
                _selectedDays.value = if (selectedDays.value == allDays) {
                    emptyList()
                } else {
                    allDays
                }
            }
            is AddHabitEvent.OnColorChanged -> {
                _color.value = event.value
            }
            is AddHabitEvent.OnDailyGoalTextChanged -> {
                _dailyEffort.value = event.value.toFloatOrNull()
            }
            is AddHabitEvent.OnDailyGoalUnitChanged -> {
                _effortUnit.value = event.unit
            }
            is AddHabitEvent.OnDayChanged -> {
                val currentDays = selectedDays.value.toMutableList()
                if (event.isChecked) {
                    currentDays.add(event.day)
                } else {
                    currentDays.remove(event.day)
                }
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
        }
    }
}