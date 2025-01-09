package com.grzeluu.habittracker.feature.onboarding.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.feature.onboarding.ui.animations.OnboardingAnimations

@Composable
fun NavigateUpArrow(
    pagerState: PagerState,
    goToPreviousPage: () -> Unit
) {
    AnimatedVisibility(
        visible = pagerState.currentPage != 0,
        enter = OnboardingAnimations.enterPagerBackArrow,
        exit = OnboardingAnimations.exitPagerBackArrow,
    ) {
        IconButton(modifier = Modifier
            .padding(start = 8.dp)
            .systemBarsPadding(),
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface),
            onClick = { goToPreviousPage() }) {
            Icon(
                painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.go_to_previous_step)
            )
        }
    }
}