package com.grzeluu.habittracker.feature.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.details.ui.components.ConfirmArchiveHabitDialog
import com.grzeluu.habittracker.feature.details.ui.components.ConfirmDeleteHabitDialog
import com.grzeluu.habittracker.feature.details.ui.components.DetailsHabitEffortDialog
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTitleCard
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTopBar
import com.grzeluu.habittracker.feature.details.ui.components.HabitDetailsStatsCards
import com.grzeluu.habittracker.feature.details.ui.components.LatestEffortButtons
import com.grzeluu.habittracker.feature.details.ui.components.ChartSection
import com.grzeluu.habittracker.feature.details.ui.components.ConfirmUnarchiveHabitDialog
import com.grzeluu.habittracker.feature.details.ui.components.DetailsCardWithIcon
import com.grzeluu.habittracker.feature.details.ui.event.DetailsEvent
import com.grzeluu.habittracker.feature.details.ui.event.DetailsNavigationEvent
import com.grzeluu.habittracker.feature.details.ui.state.DetailsDataState
import com.grzeluu.habittracker.util.flow.ObserveAsEvent
import com.grzeluu.habittracker.util.numbers.formatFloat
import kotlinx.datetime.LocalDate

@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit, onNavigateToAddHabit: (Long) -> Unit
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    var isArchiveDialogVisible by remember { mutableStateOf(false) }
    var isUnarchiveDialogVisible by remember { mutableStateOf(false) }

    BackHandler {
        onNavigateBack()
    }

    ObserveAsEvent(viewModel.navigationEventsChannelFlow) { event ->
        when (event) {
            DetailsNavigationEvent.NAVIGATE_BACK -> onNavigateBack()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        DetailsTopBar(uiState = uiState,
            onNavigateBack = onNavigateBack,
            onDelete = { isDeleteDialogVisible = true },
            onEdit = { onNavigateToAddHabit(viewModel.habitId) },
            onArchive = { isArchiveDialogVisible = true })
    }) { innerPadding ->
        BaseScreenContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), uiState
        ) { uiData ->
            ConfirmDeleteHabitDialog(isVisible = isDeleteDialogVisible,
                onDismissRequest = { isDeleteDialogVisible = false },
                onDeleteConfirmed = { viewModel.onEvent(DetailsEvent.OnDeleteHabit) })

            ConfirmArchiveHabitDialog(isVisible = isArchiveDialogVisible,
                onDismissRequest = { isArchiveDialogVisible = false },
                onDeleteConfirmed = { viewModel.onEvent(DetailsEvent.OnArchiveHabit) })

            ConfirmUnarchiveHabitDialog(isVisible = isUnarchiveDialogVisible,
                onDismissRequest = { isUnarchiveDialogVisible = false },
                onDeleteConfirmed = { viewModel.onEvent(DetailsEvent.OnUnarchiveHabit) })

            DetailsScreenContent(viewModel, uiData, unarchive = { isUnarchiveDialogVisible = true })
        }
    }
}

@Composable
private fun DetailsScreenContent(
    viewModel: DetailsViewModel, uiData: DetailsDataState, unarchive: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(horizontal = AppSizes.screenPadding)
            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.Start
    ) {
        DetailsTitleCard(modifier = Modifier.fillMaxWidth(), habit = uiData.habit)
        Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections))
        if (uiData.habit.isArchive) {
            ArchivedHabitControls(uiData, unarchive)
        } else {
            ActiveHabitControls(uiData, viewModel)
        }
    }
}

@Composable
private fun ActiveHabitControls(
    uiData: DetailsDataState,
    viewModel: DetailsViewModel
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    selectedDate?.let { date ->
        DetailsHabitEffortDialog(
            habit = uiData.habit,
            selectedDate = date,
            onDismiss = { selectedDate = null },
            onSetProgress = { effort ->
                viewModel.onEvent(
                    DetailsEvent.OnSaveDailyEffort(
                        date = date, effort = effort
                    )
                )
            }
        )
    }
    HabitDetailsStatsCards(habit = uiData.habit)
    Spacer(modifier = Modifier.height(24.dp))
    BasicLabel(text = stringResource(R.string.your_latest_effort))
    Spacer(modifier = Modifier.height(4.dp))
    LatestEffortButtons(uiData.habit, uiData.lastDays) { selectedDate = it }
    Spacer(modifier = Modifier.height(24.dp))
    BasicLabel(text = stringResource(R.string.your_progress))
    ChartSection(
        habit = uiData.habit,
        periodStats = uiData.periodStats,
        onSelectPeriod = { viewModel.onEvent(DetailsEvent.OnSelectPeriod(it)) },
        selectedPeriod = uiData.selectedPeriod
    )
}

@Composable
private fun ArchivedHabitControls(
    uiData: DetailsDataState,
    unarchive: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        DetailsCardWithIcon(
            modifier = Modifier.weight(1f),
            iconPainter = painterResource(R.drawable.ic_goal),
            iconColor = uiData.habit.color.mapToColor(),
            label = stringResource(R.string.daily_goal),
            body = "${uiData.habit.effort.desiredValue.formatFloat()} ${
                uiData.habit.effort.effortUnit.mapToUiText().asString()
            }"
        )
        Spacer(modifier = Modifier.width(8.dp))
        DetailsCardWithIcon(
            modifier = Modifier.weight(1f),
            iconPainter = painterResource(R.drawable.ic_chart),
            label = stringResource(R.string.total_effort),
            iconColor = uiData.habit.color.mapToColor(),
            body = "${uiData.habit.totalEffort.formatFloat()} ${
                uiData.habit.effort.effortUnit.mapToUiText().asString()
            }"
        )
    }
    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections))
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(AppSizes.dialogPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_archive),
                contentDescription = null,
                tint = uiData.habit.color.mapToColor(),
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.habit_archived),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                modifier = Modifier.padding(top = 12.dp),
                onClick = unarchive
            ) {
                Text(
                    text = stringResource(R.string.unarchive)
                )
            }
        }
    }
}






