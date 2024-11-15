package com.grzeluu.habittracker.common.ui.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    loadingText: UiText = UiText.StringResource(R.string.please_wait),
    loadingViewColors: LoadingViewColors = LoadingViewDefaults.loadingViewColors()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(loadingViewColors.backgroundColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(104.dp ),
                strokeWidth = 6.dp,
                color = loadingViewColors.indicatorColor
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = loadingText.asString(),
                color = loadingViewColors.textColor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

class LoadingViewColors(
    val backgroundColor: Color,
    val textColor: Color,
    val indicatorColor: Color,
)