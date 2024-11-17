package com.grzeluu.habittracker.feature.habits.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.habits.ui.components.DaysOfWeekRow
import com.grzeluu.habittracker.feature.habits.ui.components.HabitsImageWithDescription
import com.grzeluu.habittracker.feature.habits.ui.event.HabitsEvent
import com.grzeluu.habittracker.feature.habits.ui.page.DailyPage
import com.grzeluu.habittracker.feature.habits.ui.state.HabitsDataState
import kotlinx.coroutines.launch

@Composable
fun HabitsScreen() {
    val viewModel: HabitsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    BaseScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        if (data.areHabitsAdded) {
            HabitsScreenContent(data, viewModel)
        } else {
            HabitsImageWithDescription(
                modifier = Modifier.fillMaxSize().padding(AppSizes.screenPadding),
                painter = painterResource(id = com.grzeluu.habittracker.feature.habits.R.drawable.goals),
                description = stringResource(R.string.add_your_first_habit)
            )
        }
    }
}

@Composable
private fun HabitsScreenContent(
    data: HabitsDataState,
    viewModel: HabitsViewModel
) {
    val pagerState = rememberPagerState(pageCount = { 7 }, initialPage = data.selectedDay.dayOfWeek.value - 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val selectedDay = data.daysOfWeek.getOrNull(pagerState.currentPage)?.second
        if (selectedDay != null) {
            viewModel.onEvent(HabitsEvent.OnChangeSelectedDay(selectedDay))
        }
    }

    Column(Modifier.fillMaxSize()) {
        DaysOfWeekRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSizes.screenPadding),
            selectedDay = data.selectedDay,
            daysOfWeek = data.daysOfWeek,
            onDayClicked = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it.dayOfWeek.value - 1)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            DailyPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppSizes.screenPadding),
                date = data.daysOfWeek[pageIndex].second,
            )
        }
    }
}