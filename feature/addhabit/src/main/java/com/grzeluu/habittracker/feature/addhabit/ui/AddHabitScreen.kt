package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenIconAndText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.common.ui.topbar.BasicTopAppBar
import com.grzeluu.habittracker.feature.addhabit.ui.components.ColorSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.DaySelectionView
import com.grzeluu.habittracker.feature.addhabit.ui.components.IconSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetDailyGoalView
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetNotificationsView
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.util.enums.EffortUnit

@Composable
fun AddHabitScreen(
    onNavigateToMainPage: () -> Unit,
) {

    val viewModel: AddHabitViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        onNavigateToMainPage()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = stringResource(R.string.add_habit),
                onNavigateBack = onNavigateToMainPage
            )
        },
    ) { innerPadding ->
        BaseScreenContainer(
            modifier = Modifier.padding(innerPadding),
            uiState
        ) { uiData ->
            Column(
                modifier = Modifier
                    .padding(horizontal = AppSizes.screenPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                CustomTextField(
                    value = uiData.name,
                    imeAction = ImeAction.Done,
                    onValueChange = { viewModel.onEvent(AddHabitEvent.OnNameChanged(it)) },
                    label = stringResource(R.string.name)
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
                CustomTextField(
                    maxLines = 2,
                    imeAction = ImeAction.Done,
                    value = uiData.description.orEmpty(),
                    onValueChange = { viewModel.onEvent(AddHabitEvent.OnDescriptionChanged(it)) },
                    label = stringResource(R.string.description)
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormSections))
                BasicLabel(text = stringResource(R.string.color))
                ColorSelectionRow(
                    selectedColor = uiData.color,
                    onSelectionChanged = { viewModel.onEvent(AddHabitEvent.OnColorChanged(it)) }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
                BasicLabel(text = stringResource(R.string.icon))
                IconSelectionRow(
                    selectedIcon = uiData.icon,
                    iconsColor = uiData.color,
                    onSelectionChanged = { viewModel.onEvent(AddHabitEvent.OnIconChanged(it)) }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormSections))
                DaySelectionView(
                    selectedDays = uiData.selectedDays,
                    onDayCheckedChange = { day, isChecked ->
                        viewModel.onEvent(AddHabitEvent.OnDayChanged(day, isChecked))
                    },
                    toggleSelectAll = { viewModel.onEvent(AddHabitEvent.OnAllDaysToggled) }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormSections))
                BasicLabel(text = stringResource(R.string.set_your_daily_goal))
                SetDailyGoalView(
                    goalTextState = uiData.dailyEffort.toString(),
                    onTextChanged = { viewModel.onEvent(AddHabitEvent.OnDailyGoalTextChanged(it)) },
                    selectedEffortUnit = uiData.effortUnit,
                    onChangeEffortUnit = { viewModel.onEvent(AddHabitEvent.OnDailyGoalUnitChanged(it)) }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
                SetNotificationsView(
                    isNotificationsEnabled = uiData.isNotificationsEnabled,
                    onNotificationsEnabledChange = { viewModel.onEvent(AddHabitEvent.OnNotificationsEnabledChanged(it)) }
                )
                Spacer(modifier = Modifier.defaultMinSize(AppSizes.spaceBetweenFormSections).weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = { /* TODO */ }
                ) {
                    Icon(painterResource(R.drawable.ic_add), null)
                    Text(
                        modifier = Modifier.padding(start = spaceBetweenIconAndText),
                        text = stringResource(R.string.add_habit)
                    )
                }
            }
        }
    }
}





