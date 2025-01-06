package com.grzeluu.habittracker.feature.home.ui

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.home.ui.components.DaysOfWeekRow
import com.grzeluu.habittracker.feature.home.ui.components.HabitsImageWithDescription
import com.grzeluu.habittracker.feature.home.ui.event.HomeEvent
import com.grzeluu.habittracker.feature.home.ui.page.DailyPage
import com.grzeluu.habittracker.feature.home.ui.state.HomeDataState
import kotlinx.coroutines.launch

@Composable
fun HabitsScreen(
    onNavigateToDetails: (habitId: Long) -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    BaseScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        if (data.areHabitsAdded) {
            HabitsScreenContent(data, viewModel, onNavigateToDetails)
        } else {
            HabitsImageWithDescription(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSizes.screenPadding),
                painter = painterResource(id = com.grzeluu.habittracker.feature.home.R.drawable.goals),
                description = stringResource(R.string.add_your_first_habit)
            )
        }
    }
}

@Composable
private fun HabitsScreenContent(
    data: HomeDataState,
    viewModel: HomeViewModel,
    onNavigateToDetails: (habitId: Long) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 7 }, initialPage = data.selectedDay.dayOfWeek.value - 1)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        val selectedDay = data.daysOfWeek.getOrNull(pagerState.currentPage)
        if (selectedDay != null) {
            viewModel.onEvent(HomeEvent.OnChangeSelectedDay(selectedDay))
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
        Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections))
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            DailyPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppSizes.screenPadding),
                date = data.daysOfWeek[pageIndex],
                onNavigateToDetails = onNavigateToDetails
            )
        }
    }
}