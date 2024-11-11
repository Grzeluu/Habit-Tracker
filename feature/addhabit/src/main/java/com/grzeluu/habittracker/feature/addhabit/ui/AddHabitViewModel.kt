package com.grzeluu.habittracker.feature.addhabit.ui

import com.grzeluu.habittracker.base.ui.BaseViewModel
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.state.AddHabitStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor() : BaseViewModel<AddHabitStateData>() {



    override val uiDataState: StateFlow<AddHabitStateData?>
        get() = TODO("Not yet implemented")

    fun onEvent(event: AddHabitEvent) {
        when (event) {
            AddHabitEvent.OnAllDaysToggled -> TODO()
            is AddHabitEvent.OnColorChanged -> TODO()
            is AddHabitEvent.OnDailyGoalTextChanged -> TODO()
            is AddHabitEvent.OnDailyGoalUnitChanged -> TODO()
            is AddHabitEvent.OnDayChanged -> TODO()
            is AddHabitEvent.OnDescriptionChanged -> TODO()
            is AddHabitEvent.OnIconChanged -> TODO()
            is AddHabitEvent.OnNameChanged -> TODO()
            is AddHabitEvent.OnNotificationsEnabledChanged -> TODO()
        }
    }
}