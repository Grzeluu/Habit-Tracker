package com.grzeluu.habittracker.feature.addhabit.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenFormSections
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenIconAndText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.common.ui.topbar.BasicTopAppBar
import com.grzeluu.habittracker.feature.addhabit.ui.components.ColorSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.DaySelectionView
import com.grzeluu.habittracker.feature.addhabit.ui.components.IconSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetDailyGoalView
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetNotificationsView
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitNavigationEvent
import com.grzeluu.habittracker.util.flow.ObserveAsEvent

@Composable
fun AddHabitScreen(
    onNavigateBack: () -> Unit,
) {

    val viewModel: AddHabitViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.navigationEventsChannelFlow) { event ->
        when (event) {
            AddHabitNavigationEvent.NAVIGATE_AFTER_SAVE -> onNavigateBack()
        }
    }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = if (viewModel.habitId != null) stringResource(R.string.edit_habit) else stringResource(R.string.add_habit),
                onNavigateBack = onNavigateBack,
            )
        },
    ) { innerPadding ->
        BaseScreenContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            uiState
        ) { uiData ->
            Column(
                modifier = Modifier
                    .padding(horizontal = AppSizes.screenPadding)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                val nameMaxLength = 30
                CustomTextField(
                    value = uiData.nameField.value,
                    isError = uiData.nameField.errorMassage != null,
                    imeAction = ImeAction.Done,
                    onValueChange = {
                        if (it.count() <= nameMaxLength)
                            viewModel.onEvent(AddHabitEvent.OnNameChanged(it))
                    },
                    label = stringResource(R.string.name),
                    supportingText = {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceBetween
                        ) {
                            Text(uiData.nameField.errorMassage?.asString() ?: stringResource(R.string.required_field))
                            AnimatedVisibility(visible = uiData.nameField.value.isNotEmpty()) {
                                Text("${uiData.nameField.value.count()}/$nameMaxLength")
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
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
                Spacer(modifier = Modifier.height(spaceBetweenFormSections))
                DaySelectionView(
                    selectedDays = uiData.selectedDaysField.value,
                    onDayCheckedChange = { day, isChecked ->
                        viewModel.onEvent(AddHabitEvent.OnDayChanged(day, isChecked))
                    },
                    toggleSelectAll = { viewModel.onEvent(AddHabitEvent.OnAllDaysToggled) },
                    isError = uiData.selectedDaysField.errorMassage != null,
                    supportingText = uiData.selectedDaysField.errorMassage?.asString()
                        ?: stringResource(R.string.select_at_least_one_day)
                )
                Spacer(modifier = Modifier.height(spaceBetweenFormSections))
                SetDailyGoalView(
                    goalTextState = uiData.dailyEffort.orEmpty(),
                    onTextChanged = { viewModel.onEvent(AddHabitEvent.OnDailyGoalTextChanged(it)) },
                    selectedEffortUnit = uiData.effortUnit,
                    onChangeEffortUnit = { viewModel.onEvent(AddHabitEvent.OnDailyGoalUnitChanged(it)) }
                )
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
                val descriptionMaxLength = 50
                CustomTextField(
                    maxLines = 2,
                    imeAction = ImeAction.Done,
                    value = uiData.description.orEmpty(),
                    onValueChange = {
                        if (it.count() <= descriptionMaxLength) {
                            viewModel.onEvent(AddHabitEvent.OnDescriptionChanged(it))
                        }
                    },
                    label = stringResource(R.string.description),
                    supportingText = {
                        AnimatedVisibility(visible = !uiData.description.isNullOrEmpty()) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "${uiData.description?.count() ?: 0}/$descriptionMaxLength",
                                textAlign = TextAlign.End
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(spaceBetweenFormSections))
                SetNotificationsView(
                    isNotificationsEnabled = uiData.isNotificationsEnabled,
                    onNotificationsEnabledChange = { viewModel.onEvent(AddHabitEvent.OnNotificationsEnabledChanged(it)) }
                )
                Spacer(modifier = Modifier.height(spaceBetweenFormSections))
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = AppSizes.screenPadding)
                        .align(Alignment.CenterHorizontally),
                    onClick = { viewModel.onEvent(AddHabitEvent.AddHabit) }
                ) {
                    Icon(
                        painterResource(if (viewModel.habitId != null) R.drawable.ic_edit else R.drawable.ic_add),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = spaceBetweenIconAndText),
                        text = stringResource(if (viewModel.habitId != null) R.string.edit_habit else R.string.add_habit)
                    )
                }
            }
        }
    }
}





