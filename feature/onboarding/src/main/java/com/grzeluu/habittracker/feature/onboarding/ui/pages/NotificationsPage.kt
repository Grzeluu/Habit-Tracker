package com.grzeluu.habittracker.feature.onboarding.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun NotificationsPage(
    isNotificationsEnabled: Boolean,
    goToNextPage: () -> Unit,
    changeIsNotificationsEnabled: (Boolean) -> Unit
) {
    OnboardingContentPage(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        imagePainter = painterResource(id = R.drawable.notifications),
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        buttonText = stringResource(comR.string.cont),
        goToNextPage = goToNextPage
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(12.dp, 0.dp),
                text = stringResource(comR.string.notifications),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Switch(
                checked = isNotificationsEnabled,
                onCheckedChange = changeIsNotificationsEnabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                    checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                )
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp, 12.dp),
            textAlign = TextAlign.Center,
            text = stringResource(comR.string.choose_notifications),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}