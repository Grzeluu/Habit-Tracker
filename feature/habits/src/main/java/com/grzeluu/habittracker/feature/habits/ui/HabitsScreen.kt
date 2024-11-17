package com.grzeluu.habittracker.feature.habits.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.feature.habits.ui.components.DaysOfWeekRow
import com.grzeluu.habittracker.feature.habits.ui.components.HabitCard
import com.grzeluu.habittracker.feature.habits.ui.components.HabitStatisticsCard
import com.grzeluu.habittracker.feature.habits.ui.event.HabitsEvent
import com.grzeluu.habittracker.util.date.getCurrentDate
import com.grzeluu.habittracker.util.enums.Day

@Composable
fun HabitsScreen() {
    val viewModel: HabitsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    BaseScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = AppSizes.screenPadding)
        ) {
            DaysOfWeekRow(
                modifier = Modifier.fillMaxWidth(),
                selectedDay = data.selectedDay,
                daysOfWeek = data.daysOfWeek,
                onDayClicked = { viewModel.onEvent(HabitsEvent.OnChangeSelectedDay(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            HabitStatisticsCard(
                modifier = Modifier.fillMaxWidth(),
                totalHabits = data.dailyStatistics.totalHabits,
                habitsDone = data.dailyStatistics.habitsDone,
                currentEffort = data.dailyStatistics.totalProgress
            )
            Spacer(modifier = Modifier.height(12.dp))
            BasicLabel(
                text =
                if (data.selectedDay == getCurrentDate()) stringResource(R.string.today_habits)
                else stringResource(
                    R.string.day_habits,
                    Day.get(data.selectedDay.dayOfWeek.value).mapToUiText(isShort = false).asString()
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