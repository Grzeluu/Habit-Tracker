package com.grzeluu.habittracker.common.ui.loading

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object LoadingViewDefaults {

    @Composable
    fun loadingViewColors(
        backgroundColor: Color = MaterialTheme.colorScheme.surface,
        textColor: Color = MaterialTheme.colorScheme.onSurface,
        indicatorColor: Color = MaterialTheme.colorScheme.primary,
    ): LoadingViewColors = LoadingViewColors(
        backgroundColor = backgroundColor,
        textColor = textColor,
        indicatorColor = indicatorColor,
    )

}