package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.background.FilledBackground
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.numbers.formatFloat
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    habitInfo: DailyHabitInfo,
    onButtonClicked: () -> Unit,
    onCardClicked: () -> Unit,
) {
    val filled = (habitInfo.dailyHistoryEntry?.currentEffort ?: 0f) / habitInfo.effort.desiredValue
    val effortString =
        buildString {
            with(habitInfo) {
                if (currentEffort > 0) {
                    append(currentEffort.formatFloat())
                    append(" / ")
                }
                append(effort.desiredValue.formatFloat())
                append(" ")
                append(effort.effortUnit.mapToUiText().asString())
            }
        }



    Card(
        modifier = modifier,
        onClick = onCardClicked,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Box(Modifier.fillMaxWidth()) {
            FilledBackground(
                modifier = Modifier.fillMaxWidth(),
                color = habitInfo.color.mapToColor().copy(alpha = 0.25f),
                fill = filled
            )
            Row(
                modifier = Modifier.padding(AppSizes.cardInnerPadding)
            ) {
                Icon(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 8.dp)
                        .size(28.dp),
                    painter = painterResource(habitInfo.icon.mapToDrawableRes()),
                    contentDescription = null,
                    tint = habitInfo.color.mapToColor(),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = habitInfo.name,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!habitInfo.description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = habitInfo.description.orEmpty(),
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = effortString,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterVertically),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor =
                        if (habitInfo.effortProgress > 0f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    ),
                    onClick = onButtonClicked,
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(
                            when {
                                habitInfo.effortProgress >= 1f -> R.drawable.ic_checked_filled
                                habitInfo.effortProgress > 0f -> R.drawable.ic_add_circle
                                else -> R.drawable.ic_checked
                            }
                        ),
                        contentDescription = stringResource(R.string.done),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HabitCardPreviewDone1() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habitInfo = DailyHabitInfo(
                name = "Drink water",
                icon = CardIcon.DRINK,
                color = CardColor.BLUE,
                description = "At least 2.5l daily",
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.LITERS,
                    desiredValue = 2.5f,
                ),
                dailyHistoryEntry = HabitHistoryEntry(
                    date = Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date,
                    currentEffort = 2.5f,
                    note = null,
                ),
            ),
            onButtonClicked = {},
            onCardClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardPreviewAlmostDone1() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habitInfo = DailyHabitInfo(
                name = "Running",
                icon = CardIcon.RUN,
                color = CardColor.RED,
                description = "5km - moderate pace",
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.KM,
                    desiredValue = 5f,
                ),
                dailyHistoryEntry = HabitHistoryEntry(
                    date = Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date,
                    currentEffort = 4f,
                    note = null,
                ),
            ),
            onButtonClicked = {},
            onCardClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardPreviewAlmostDone2() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habitInfo = DailyHabitInfo(
                name = "Reading",
                icon = CardIcon.BOOK,
                color = CardColor.GREEN,
                description = "30 pages daily",
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.KM,
                    desiredValue = 5f,
                ),
                dailyHistoryEntry = HabitHistoryEntry(
                    date = Clock.System.now().toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).date,
                    currentEffort = 2.5f,
                    note = null,
                ),
            ),
            onButtonClicked = {},
            onCardClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardNotDone() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habitInfo = DailyHabitInfo(
                name = "Rest",
                icon = CardIcon.WELLNESS,
                color = CardColor.PURPLE,
                description = null,
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.REPEAT,
                    desiredValue = 1f,
                ),
                dailyHistoryEntry = null,
            ),
            onButtonClicked = {},
            onCardClicked = {}
        )
    }
}