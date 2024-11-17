package com.grzeluu.habittracker.feature.habits.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.habits.ui.components.HabitCard
import com.grzeluu.habittracker.feature.habits.ui.components.HabitStatisticsCard
import com.grzeluu.habittracker.util.date.getCurrentDate
import com.grzeluu.habittracker.util.enums.Day
import kotlinx.datetime.LocalDate

@Composable
fun DailyPage(
    modifier: Modifier = Modifier,
    date: LocalDate
) {

    val viewModel: DailyViewModel = hiltViewModel<DailyViewModel, DailyViewModel.DailyViewModelFactory>(
        key = date.toString()
    ) { factory ->
        factory.create(date)
    }
    val uiState by viewModel.uiState.collectAsState()

    BaseScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
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
                    Day.get(viewModel.date.dayOfWeek.value).mapToUiText(isShort = false).asString()
                )
            )
            LazyColumn {
                items(data.dailyHabits, key = { it.id }) {
                    HabitCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),

                        habitInfo = it,
                        onButtonClicked = { /* TODO */ }
                    )
                    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenCards))
                }
            }
        }
    }
}