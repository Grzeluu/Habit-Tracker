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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grzeluu.habittracker.base.ui.UiStateScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenFormElements
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenIconAndText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.common.ui.topbar.BasicTopAppBar
import com.grzeluu.habittracker.feature.addhabit.ui.components.ColorSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.DaySelectionView
import com.grzeluu.habittracker.feature.addhabit.ui.components.IconSelectionRow
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetDailyGoalView
import com.grzeluu.habittracker.feature.addhabit.ui.components.SetNotificationsView
import com.grzeluu.habittracker.feature.addhabit.ui.components.TimePickerDialog
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitEvent
import com.grzeluu.habittracker.feature.addhabit.ui.event.AddHabitNavigationEvent
import com.grzeluu.habittracker.util.flow.ObserveAsEvent
import kotlinx.coroutines.launch

@Composable
fun AddHabitScreen(
    onNavigateBack: () -> Unit,
) {

    val viewModel: AddHabitViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isNotificationTimeDialogVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            BasicTopAppBar(
                title = if (viewModel.habitId != null) stringResource(R.string.edit_habit) else stringResource(R.string.add_habit),
                onNavigateBack = onNavigateBack,
                actions = {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = { viewModel.onEvent(AddHabitEvent.AddHabit) }
                    ) {
                        Icon(
                            painterResource(if (viewModel.habitId != null) R.drawable.ic_edit else R.drawable.ic_add),
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.padding(start = spaceBetweenIconAndText),
                            text = stringResource(if (viewModel.habitId != null) R.string.edit else R.string.add)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        UiStateScreenContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            uiState
        ) { uiData ->
            TimePickerDialog(
                isVisible = isNotificationTimeDialogVisible,
                selectedTime = uiData.notificationSettings.time,
                onDismissRequest = { isNotificationTimeDialogVisible = false },
                onTimeSelected = { viewModel.onEvent(AddHabitEvent.OnNotificationTimeChanged(it)) }
            )

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
                Spacer(modifier = Modifier.height(spaceBetweenFormElements))
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
                Spacer(modifier = Modifier.height(spaceBetweenFormElements))
                SetDailyGoalView(
                    goalTextState = uiData.dailyEffort.orEmpty(),
                    onTextChanged = { viewModel.onEvent(AddHabitEvent.OnDailyGoalTextChanged(it)) },
                    selectedEffortUnit = uiData.effortUnit,
                    onChangeEffortUnit = { viewModel.onEvent(AddHabitEvent.OnDailyGoalUnitChanged(it)) }
                )
                Spacer(modifier = Modifier.height(spaceBetweenFormElements))
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
                val notificationSnackbarText = stringResource(R.string.to_enable_notifications_please_enable_push_notifications_in_settings)
                SetNotificationsView(
                    notificationSettings = uiData.notificationSettings,
                    onNotificationsEnabledChange = {
                        if (uiData.isPushNotificationsEnabled) {
                            viewModel.onEvent(AddHabitEvent.OnNotificationsEnabledChanged(it))
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(notificationSnackbarText)
                            }
                        }
                    },
                    onShowTimePicker = { isNotificationTimeDialogVisible = true }
                )
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}





