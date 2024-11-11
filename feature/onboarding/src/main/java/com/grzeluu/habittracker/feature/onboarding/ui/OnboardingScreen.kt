package com.grzeluu.habittracker.feature.onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.feature.onboarding.ui.animations.OnboardingAnimations
import com.grzeluu.habittracker.feature.onboarding.ui.components.PagerIndicator
import com.grzeluu.habittracker.feature.onboarding.ui.event.NavigationEvent
import com.grzeluu.habittracker.feature.onboarding.ui.pages.AddHabitPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.NotificationsPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.ThemePage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.WelcomePage
import com.grzeluu.habittracker.util.flow.ObserveAsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onNavigateToMainPage: () -> Unit,
    onNavigateToAddHabit: () -> Unit
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvent(viewModel.navigationEventsChannelFlow) { event ->
        when (event) {
            NavigationEvent.NAVIGATE_TO_MAIN_PAGE -> onNavigateToMainPage()
            NavigationEvent.NAVIGATE_TO_ADD_HABIT -> onNavigateToAddHabit()
        }
    }

    BaseScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        Box {
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> WelcomePage(goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                    1 -> ThemePage(isDarkModeEnabled = data.isDarkModeEnabled ?: isSystemInDarkTheme(),
                        changeIsDarkModeSelected = { viewModel.changeDarkModeSettings(it) },
                        goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                    2 -> NotificationsPage(isNotificationsEnabled = data.isNotificationsEnabled,
                        changeIsNotificationsEnabled = { viewModel.changeNotificationSettings(it) },
                        goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                    3 -> AddHabitPage(
                        goToAddHabit = { viewModel.saveSettingsAndNavigate(NavigationEvent.NAVIGATE_TO_ADD_HABIT) },
                        goToApp = { viewModel.saveSettingsAndNavigate(NavigationEvent.NAVIGATE_TO_MAIN_PAGE) },
                    )
                }
            }
            AnimatedVisibility(
                visible = pagerState.currentPage != 0,
                enter = OnboardingAnimations.enterPagerBackArrow,
                exit = OnboardingAnimations.exitPagerBackArrow,
            ) {
                IconButton(modifier = Modifier
                    .padding(start = 8.dp)
                    .systemBarsPadding(),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                    onClick = { goToPreviousPage(coroutineScope, pagerState) }) {
                    Icon(
                        painterResource(R.drawable.ic_back),
                        contentDescription = stringResource(R.string.go_to_previous_step)
                    )
                }
            }
            PagerIndicator(
                modifier = Modifier
                    .systemBarsPadding()
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp), pagerState = pagerState
            )
        }
    }
}

private fun goToPreviousPage(coroutineScope: CoroutineScope, pagerState: PagerState) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
}

private fun goToNextPage(coroutineScope: CoroutineScope, pagerState: PagerState) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
}