package com.grzeluu.habittracker.feature.onboarding.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.feature.onboarding.R
import com.grzeluu.habittracker.feature.onboarding.ui.components.OnboardingContentPage
import com.grzeluu.habittracker.common.ui.R as comR

@Composable
fun ThemePage(
    isDarkModeEnabled: Boolean,
    goToNextPage: () -> Unit,
    changeIsDarkModeSelected: (Boolean) -> Unit
) {
    OnboardingContentPage(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        imagePainter = painterResource(id = R.drawable.day_night),
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        buttonText = stringResource(comR.string.cont),
        goToNextPage = goToNextPage
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(12.dp, 0.dp),
                text = stringResource(comR.string.dark_mode),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = changeIsDarkModeSelected
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp, 12.dp),
            textAlign = TextAlign.Center,
            text = stringResource(comR.string.chooseDarkMode),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }
}