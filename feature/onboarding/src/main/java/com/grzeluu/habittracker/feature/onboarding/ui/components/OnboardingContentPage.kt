package com.grzeluu.habittracker.feature.onboarding.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingContentPage(
    modifier: Modifier = Modifier,
    imagePainter: Painter,
    backgroundColor: Color,
    buttonColors: ButtonColors,
    buttonText: String,
    goToNextPage: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 48.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier.fillMaxWidth(0.8f),
                painter = imagePainter,
                contentDescription = null
            )
            content()
            Spacer(modifier = Modifier.weight(1f))
            ElevatedButton(
                colors = buttonColors,
                onClick = goToNextPage
            ) {
                Text(
                    text = buttonText,
                )
            }
        }
    }
}