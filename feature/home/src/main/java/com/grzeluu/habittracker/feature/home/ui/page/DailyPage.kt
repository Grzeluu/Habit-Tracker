package com.grzeluu.habittracker.feature.home.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.feature.home.ui.components.HabitCard
import com.grzeluu.habittracker.common.ui.dialog.HabitEffortDialog
import com.grzeluu.habittracker.feature.home.ui.components.HabitStatisticsCard
import com.grzeluu.habittracker.feature.home.ui.components.HabitsImageWithDescription
import com.grzeluu.habittracker.feature.home.ui.event.DailyEvent
import com.grzeluu.habittracker.feature.home.ui.state.DailyDataState
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

@Composable
fun DailyPage(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onNavigateToDetails: (habitId: Long) -> Unit
) {

    val viewModel: DailyViewModel = hiltViewModel<DailyViewModel, DailyViewModel.DailyViewModelFactory>(
        key = date.toString()
    ) { factory ->
        factory.create(date)
    }
    val uiState by viewModel.uiState.collectAsState()
    var selectedHabit by remember { mutableStateOf<DailyHabitInfo?>(null) }

    selectedHabit?.let { habit ->
        HabitEffortDialog(
            habitName = habit.name,
            isDialogVisible = true,
            currentEffort = habit.currentEffort,
            habitIcon = habit.icon,
            habitColor = habit.color,
            habitDescription = habit.description,
            effortUnit = habit.effort.effortUnit,
            desiredEffortValue = habit.effort.desiredValue,
            onDismissRequest = { selectedHabit = null },
            onSetProgress = { effort ->
                selectedHabit?.let { viewModel.onEvent(DailyEvent.OnSaveDailyEffort(habitId = it.id, effort = effort)) }
            },
        )
    }

    BaseScreenContainer(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        if (data.dailyHabits.isEmpty()) {
            HabitsImageWithDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSizes.screenPadding),
                painter = painterResource(id = com.grzeluu.habittracker.feature.home.R.drawable.goals),
                description = stringResource(R.string.no_habits_for_this_day)
            )
        } else {
            DailyHabitsContent(
                modifier = Modifier.fillMaxSize(),
                data = data,
                date = date,
                onHabitEffortClicked = { selectedHabit = it },
                onNavigateToDetails = onNavigateToDetails
            )
        }
    }
}

@Composable
private fun DailyHabitsContent(
    modifier: Modifier,
    data: DailyDataState,
    date: LocalDate,
    onHabitEffortClicked: (DailyHabitInfo) -> Unit,
    onNavigateToDetails: (habitId: Long) -> Unit
) {
    Column(modifier) {
        HabitStatisticsCard(
            modifier = Modifier.fillMaxWidth(),
            totalHabits = data.dailyStatistics.totalHabits,
            habitsDone = data.dailyStatistics.habitsDone,
            currentEffort = data.dailyStatistics.totalProgress
        )
        Spacer(modifier = Modifier.height(12.dp))
        BasicLabel(
            text =
            if (date == getCurrentDate()) stringResource(R.string.today_habits)
            else stringResource(
                R.string.day_habits,
                Day.get(date.dayOfWeek.value).mapToUiText(isShort = false).asString()
            )
        )
        LazyColumn {
            itemsIndexed(data.dailyHabits, key = { _, habit -> habit.id }) { index, it ->
                HabitCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),

                    habitInfo = it,
                    onButtonClicked = { onHabitEffortClicked(it) },
                    onCardClicked = { onNavigateToDetails(it.id) }
                )
                if (index != data.dailyHabits.lastIndex) {
                    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenCards))
                }
            }
            item {
                Spacer(modifier = Modifier.height(AppSizes.fabSpacer))
            }
        }
    }
}