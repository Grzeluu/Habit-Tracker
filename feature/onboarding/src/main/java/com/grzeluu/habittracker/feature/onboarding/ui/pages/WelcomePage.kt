package com.grzeluu.habittracker.feature.onboarding.ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.feature.onboarding.R
import com.grzeluu.habittracker.feature.onboarding.ui.components.OnboardingContentPage
import com.grzeluu.habittracker.common.ui.R as comR

@Composable
fun WelcomePage(
    goToNextPage: () -> Unit
) {
    OnboardingContentPage(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        imagePainter = painterResource(id = R.drawable.welcome),
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        buttonText = stringResource(comR.string.lets_begin),
        goToNextPage = goToNextPage
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp, 12.dp),
            textAlign = TextAlign.Center,
            text = stringResource(comR.string.onboarding_welcome_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp, 0.dp),
            textAlign = TextAlign.Center,
            text = stringResource(comR.string.onboarding_welcome_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}