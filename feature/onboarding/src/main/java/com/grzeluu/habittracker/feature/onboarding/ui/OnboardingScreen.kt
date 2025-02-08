package com.grzeluu.habittracker.feature.onboarding.ui

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.UiStateScreenContainer
import com.grzeluu.habittracker.common.ui.permission.NotificationPermissionRationale
import com.grzeluu.habittracker.common.ui.permission.permissionLauncher
import com.grzeluu.habittracker.feature.onboarding.ui.components.NavigateUpArrow
import com.grzeluu.habittracker.feature.onboarding.ui.components.PagerIndicator
import com.grzeluu.habittracker.feature.onboarding.ui.event.OnboardingEvent
import com.grzeluu.habittracker.feature.onboarding.ui.event.OnboardingNavigationEvent
import com.grzeluu.habittracker.feature.onboarding.ui.pages.AddHabitPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.NotificationsPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.ThemePage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.WelcomePage
import com.grzeluu.habittracker.feature.onboarding.ui.state.OnboardingStateData
import com.grzeluu.habittracker.util.flow.ObserveAsEvent
import com.grzeluu.habittracker.util.permissions.checkNotificationPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onNavigateToMainPage: () -> Unit,
    onNavigateToAddHabit: () -> Unit
) {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var isNotificationPermissionDialogVisible by remember { mutableStateOf(false) }
    val launcher = permissionLauncher(
        onPermissionGranted = { viewModel.onEvent(OnboardingEvent.OnChangeNotifications(true)) },
        onPermissionDenied = { isNotificationPermissionDialogVisible = true }
    )

    ObserveAsEvent(viewModel.navigationEventsChannelFlow) { event ->
        when (event) {
            OnboardingNavigationEvent.NAVIGATE_TO_MAIN_PAGE -> onNavigateToMainPage()
            OnboardingNavigationEvent.NAVIGATE_TO_ADD_HABIT -> onNavigateToAddHabit()
        }
    }

    UiStateScreenContainer(
        modifier = Modifier.fillMaxSize(),
        uiState = uiState,
    ) { data ->
        Box {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(pageCount = { 4 })
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                ShowCorrectPage(page, coroutineScope, pagerState, data, viewModel, context, launcher)
            }
            NavigateUpArrow(pagerState) { goToPreviousPage(coroutineScope, pagerState) }
            PagerIndicator(
                modifier = Modifier
                    .systemBarsPadding()
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp), pagerState = pagerState
            )
            NotificationPermissionRationale(
                isVisible = isNotificationPermissionDialogVisible,
                onDismissRequest = { isNotificationPermissionDialogVisible = false }
            )
        }
    }
}

@Composable
private fun ShowCorrectPage(
    page: Int,
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    data: OnboardingStateData,
    viewModel: OnboardingViewModel,
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when (page) {
        0 -> WelcomePage(goToNextPage = { goToNextPage(coroutineScope, pagerState) })

        1 -> ThemePage(isDarkModeEnabled = data.isDarkModeEnabled ?: isSystemInDarkTheme(),
            changeIsDarkModeSelected = {
                viewModel.onEvent(OnboardingEvent.OnChangeDarkMode(it))
            },
            goToNextPage = { goToNextPage(coroutineScope, pagerState) })

        2 -> NotificationsPage(isNotificationsEnabled = data.isNotificationsEnabled,
            goToNextPage = { goToNextPage(coroutineScope, pagerState) },
            changeIsNotificationsEnabled = {
                if (it) {
                    context.checkNotificationPermission(launcher) {
                        viewModel.onEvent(OnboardingEvent.OnChangeNotifications(true))
                    }
                } else {
                    viewModel.onEvent(OnboardingEvent.OnChangeNotifications(false))
                }
            })

        3 -> AddHabitPage(
            goToAddHabit = { viewModel.onEvent(OnboardingEvent.OnContinue(OnboardingNavigationEvent.NAVIGATE_TO_ADD_HABIT)) },
            goToApp = { viewModel.onEvent(OnboardingEvent.OnContinue(OnboardingNavigationEvent.NAVIGATE_TO_MAIN_PAGE)) }
        )
    }
}

private fun goToPreviousPage(coroutineScope: CoroutineScope, pagerState: PagerState) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
}

private fun goToNextPage(coroutineScope: CoroutineScope, pagerState: PagerState) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
}