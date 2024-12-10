package com.grzeluu.habittracker.feature.habits.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToDrawableRes
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.component.habit.domain.model.DailyHabitInfo
import com.grzeluu.habittracker.component.habit.domain.model.HabitDesiredEffort
import com.grzeluu.habittracker.component.habit.domain.model.HabitHistoryEntry
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.numbers.formatFloat

@Composable
fun SimpleHabitCard(
    modifier: Modifier = Modifier,
    habitInfo: DailyHabitInfo,
) {
    val effortString =
        buildString {
            with(habitInfo) {
                append(effort.desiredValue.formatFloat())
                append(" ")
                append(effort.effortUnit.mapToUiText().asString())
            }
        }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = habitInfo.color.mapToColor().copy(alpha = 0.25f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
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
                        text = habitInfo.description ?: "",
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
        }
    }
}

@Preview
@Composable
fun SimpleHabitCardPreview() {
    HabitTrackerTheme {
        SimpleHabitCard(
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
                    date = getCurrentDate(),
                    currentEffort = 2.5f,
                    note = null,
                ),
            )
        )
    }
}