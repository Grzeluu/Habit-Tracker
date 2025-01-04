package com.grzeluu.habittracker.feature.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.dialog.HabitEffortDialog
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.details.ui.components.Chart
import com.grzeluu.habittracker.feature.details.ui.components.ConfirmArchiveHabitDialog
import com.grzeluu.habittracker.feature.details.ui.components.ConfirmDeleteHabitDialog
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTitleCard
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTopBar
import com.grzeluu.habittracker.feature.details.ui.components.HabitDetailsStatsCards
import com.grzeluu.habittracker.feature.details.ui.components.LatestEffortButtons
import com.grzeluu.habittracker.feature.details.ui.event.DetailsEvent
import com.grzeluu.habittracker.feature.details.ui.event.DetailsNavigationEvent
import com.grzeluu.habittracker.feature.details.ui.state.DetailsDataState
import com.grzeluu.habittracker.util.flow.ObserveAsEvent
import kotlinx.datetime.LocalDate

@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit, onNavigateToAddHabit: (Long) -> Unit
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    var isArchiveDialogVisible by remember { mutableStateOf(false) }

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

            DetailsScreenContent(viewModel, uiData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreenContent(
    viewModel: DetailsViewModel, uiData: DetailsDataState
) {
    with(uiData.habit) {
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        selectedDate?.let { date ->
            HabitEffortDialog(habitName = name,
                entryDate = selectedDate,
                isDialogVisible = true,
                currentEffort = history.find { it.date == date }?.currentEffort ?: 0f,
                habitIcon = icon,
                habitColor = color,
                habitDescription = description,
                effortUnit = effort.effortUnit,
                desiredEffortValue = effort.desiredValue,
                onDismissRequest = { selectedDate = null },
                onSetProgress = { effort ->
                    viewModel.onEvent(
                        DetailsEvent.OnSaveDailyEffort(
                            date = date, effort = effort
                        )
                    )
                })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(horizontal = AppSizes.screenPadding)
                .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.Start
        ) {
            DetailsTitleCard(modifier = Modifier.fillMaxWidth(), habit = uiData.habit)
            Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections))
            HabitDetailsStatsCards(habit = uiData.habit)
            Spacer(modifier = Modifier.height(24.dp))
            BasicLabel(text = stringResource(R.string.your_latest_effort))
            Spacer(modifier = Modifier.height(4.dp))
            LatestEffortButtons(uiData.habit, uiData.lastDays) { selectedDate = it }
            Spacer(modifier = Modifier.height(24.dp))
            BasicLabel(text = stringResource(R.string.your_progress_stats))
            var selectedIndex by remember { mutableIntStateOf(0) }
            val options = listOf("Week", "Month", "Year")
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        border = BorderStroke(1.dp, color = color.mapToColor().copy(alpha = 0.5f)),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = color.mapToColor().copy(alpha = 0.5f),
                            activeContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        icon = {}
                    ) {
                        Text(label)
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp),
            ) {
                Chart(
                    data = listOf(
                        Pair(0.5f, "M"),
                        Pair(6f, "T"),
                        Pair(17f, "W"),
                        Pair(5f, "T"),
                        Pair(35f, "F"),
                        Pair(30f, "S"),
                        Pair(20f, "S")
                    ),
                    desiredEffort = effort.desiredValue,
                    modifier = Modifier.fillMaxSize().padding(AppSizes.cardInnerPadding),
                    graphColor = color.mapToColor()
                )
            }
        }
    }
}



