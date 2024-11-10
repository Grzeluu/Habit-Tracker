package com.grzeluu.habittracker.activity.ui.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset

object MainPageAnimations {
    val enterFAB = fadeIn(animationSpec = tween(300)) + slideIn(
        animationSpec = tween(300),
        initialOffset = { IntOffset(it.width, 0) } )

    val exitFAB = fadeOut(animationSpec = tween(300)) + slideOut(
        animationSpec = tween(300),
        targetOffset = { IntOffset(it.width, 0) }
    )
}