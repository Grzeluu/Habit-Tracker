package com.grzeluu.habittracker.feature.onboarding.ui.animations

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

object OnboardingAnimations {

    val enterPagerBackArrow = fadeIn(animationSpec = tween(300)) + slideIn(
    animationSpec = tween(300),
    initialOffset = { IntOffset(it.width, 0) }
    )

    val exitPagerBackArrow = fadeOut(animationSpec = tween(300)) + slideOut(
        animationSpec = tween(300),
        targetOffset = { IntOffset(it.width, 0) }
    )
}