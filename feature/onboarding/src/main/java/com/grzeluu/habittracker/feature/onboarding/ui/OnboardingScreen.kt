package com.grzeluu.habittracker.feature.onboarding.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.feature.onboarding.ui.animations.OnboardingAnimations
import com.grzeluu.habittracker.feature.onboarding.ui.components.PagerIndicator
import com.grzeluu.habittracker.feature.onboarding.ui.pages.AddHabitPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.NotificationsPage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.ThemePage
import com.grzeluu.habittracker.feature.onboarding.ui.pages.WelcomePage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 4 })

    Box(
        modifier = Modifier.systemBarsPadding()
    ) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> WelcomePage(goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                1 -> ThemePage(isDarkModeEnabled = true,
                    changeIsDarkModeSelected = {},
                    goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                2 -> NotificationsPage(isNotificationsEnabled = true,
                    changeIsNotificationsEnabled = {/* TODO */ },
                    goToNextPage = { goToNextPage(coroutineScope, pagerState) })

                3 -> AddHabitPage(
                    goToAddHabit = { /* TODO */ },
                    goToApp = { /* TODO */ },
                )
            }
        }
        AnimatedVisibility(
            visible = pagerState.currentPage != 0,
            enter = OnboardingAnimations.enterPagerBackArrow,
            exit = OnboardingAnimations.exitPagerBackArrow,
        ) {
            IconButton(
                modifier = Modifier.padding(8.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
                onClick = { goToPreviousPage(coroutineScope, pagerState) }
            ) {
                Icon(
                    painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.go_to_previous_step)
                )
            }
        }
        PagerIndicator(pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun goToPreviousPage(
    coroutineScope: CoroutineScope, pagerState: PagerState
) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
}

@OptIn(ExperimentalFoundationApi::class)
private fun goToNextPage(
    coroutineScope: CoroutineScope, pagerState: PagerState
) {
    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
}