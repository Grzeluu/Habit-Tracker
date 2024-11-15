package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.text.scaledSp
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.common.ui.R

@Composable
fun HabitStatisticsCard(
    modifier: Modifier = Modifier,
    totalHabits: Int,
    currentEffort: Float,
    habitsDone: Int,
) {
    val progressPercentage = if (totalHabits > 0) {
        (currentEffort / totalHabits) * 100
    } else 0f

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "${progressPercentage.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.scaledSp(),
                )
                CircularProgressIndicator(
                    progress = { progressPercentage / 100f },
                    modifier = Modifier
                        .size(72.dp),
                    trackColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    strokeWidth = 8.dp
                )
            }
            Spacer(Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        when {
                            progressPercentage >= 100f -> R.string.great_job_today
                            progressPercentage >= 90f -> R.string.you_are_on_fire
                            progressPercentage >= 50f -> R.string.keep_it_up
                            progressPercentage > 0f -> R.string.making_progress
                            else -> R.string.lets_begin
                        }
                    ),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("$habitsDone")
                        }
                        append(stringResource(R.string.of_completed, totalHabits))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun HabitStatisticsCardPreview() {
    HabitTrackerTheme {
        HabitStatisticsCard(
            totalHabits = 5,
            habitsDone = 3,
            currentEffort = 5f
        )
    }
}
