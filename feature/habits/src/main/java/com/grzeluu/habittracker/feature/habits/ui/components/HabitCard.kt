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
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.background.FilledBackground
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.numbers.formatFloat

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    habit: Habit,
    isDone: Boolean,
    currentEffort: Float,
    onButtonClicked: () -> Unit,
) {

    val filled = currentEffort / habit.effort.desired
    val effortString =
        habit.effort.let { effort ->
            buildString {
                //TODO
                if (currentEffort> 0) {
                    append(currentEffort.formatFloat())
                    append(" / ")
                }
                append(effort.desired.formatFloat())
                append(" ")
                append(effort.effortUnit.mapToUiText().asString())
            }
        }


    Card(modifier = modifier.wrapContentHeight()) {
        Box(modifier.fillMaxWidth()) {
            FilledBackground(
                modifier = Modifier.fillMaxWidth(),
                color = habit.color.mapToColor().copy(alpha = 0.4f),
                fill = filled
            )
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(habit.icon.mapToDrawableRes()),
                    contentDescription = null,
                    tint = habit.color.mapToColor().copy(alpha = 0.9f),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = habit.description ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    effortString?.let {
                        Text(
                            text = effortString,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    IconButton(
                        modifier = Modifier.size(42.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor =
                            if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        ),
                        onClick = onButtonClicked,
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(if (isDone) R.drawable.ic_checked_filled else R.drawable.ic_radio_unchecked),
                            contentDescription = stringResource(R.string.done),
                        )
                    }
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
            habit = Habit(
                name = "Running",
                icon = CardIcon.RUN,
                color = CardColor.RED,
                description = "5km - moderate pace",
                notificationTime = null,
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.KM,
                    desired = 5f,
                ),
                history = emptyList(),
                desirableDays = emptyList()
            ),
            isDone = true,
            currentEffort = 5f,
            onButtonClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardPreviewAlmostDone1() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habit = Habit(
                name = "Drink water",
                icon = CardIcon.DRINK,
                color = CardColor.BLUE,
                description = "2.5l daily",
                notificationTime = null,
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.LITERS,
                    desired = 2.5f,
                ),
                history = emptyList(),
                desirableDays = emptyList()
            ),
            currentEffort = 1.5f,
            isDone = true,
            onButtonClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardPreviewAlmostDone2() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habit = Habit(
                name = "Reading",
                icon = CardIcon.BOOK,
                color = CardColor.GREEN,
                description = "20 pages",
                notificationTime = null,
                effort = HabitDesiredEffort(
                    effortUnit = EffortUnit.TIMES,
                    desired = 20f,
                ),
                history = emptyList(),
                desirableDays = emptyList()
            ),
            currentEffort = 10f,
            isDone = true,
            onButtonClicked = {}
        )
    }
}

@Preview
@Composable
fun HabitCardNotDone() {
    HabitTrackerTheme {
        HabitCard(
            modifier = Modifier.wrapContentHeight(),
            habit = Habit(
                name = "Rest",
                icon = CardIcon.WELLNESS,
                color = CardColor.PURPLE,
                description = "Rest for a while",
                notificationTime = null,
                effort = HabitDesiredEffort(
                    EffortUnit.TIMES,
                    1f
                ),
                history = emptyList(),
                desirableDays = emptyList()
            ),
            currentEffort = 0f,
            isDone = false,
            onButtonClicked = {}
        )
    }
}