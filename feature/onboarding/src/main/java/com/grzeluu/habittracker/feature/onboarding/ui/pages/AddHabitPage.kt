package com.grzeluu.habittracker.feature.onboarding.ui.pages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.padding.AppSizes.spaceBetweenIconAndText
import com.grzeluu.habittracker.feature.onboarding.R
import com.grzeluu.habittracker.feature.onboarding.ui.components.OnboardingContentPage
import com.grzeluu.habittracker.common.ui.R as comR

@Composable
fun AddHabitPage(
    goToAddHabit: () -> Unit,
    goToApp: () -> Unit,
) {
    OnboardingContentPage(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
        imagePainter = painterResource(id = R.drawable.habit),
        buttonColors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        buttonText = stringResource(comR.string.skip),
        goToNextPage = goToApp
    ) {
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(vertical = 12.dp),
            onClick = goToAddHabit,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(painterResource(comR.drawable.ic_add), stringResource(comR.string.add_habit))
            Spacer(modifier = Modifier.padding(spaceBetweenIconAndText))
            Text(stringResource(comR.string.add_habit))
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            textAlign = TextAlign.Center,
            text = stringResource(comR.string.add_your_first_habit_massage),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )

    }
}