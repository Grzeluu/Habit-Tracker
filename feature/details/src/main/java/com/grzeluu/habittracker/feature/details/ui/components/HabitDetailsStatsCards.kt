package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.component.habit.domain.model.HabitNotificationSetting
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import com.grzeluu.habittracker.util.numbers.formatFloat

@Composable
fun HabitDetailsStatsCards(
    habit: Habit
) {
    with(habit) {
        Row {
            DetailsCardWithIcon(
                modifier = Modifier.weight(1f),
                iconPainter = painterResource(R.drawable.ic_goal),
                iconColor = color.mapToColor(),
                label = stringResource(R.string.daily_goal),
                body = "${effort.desiredValue.formatFloat()} ${effort.effortUnit.mapToUiText().asString()}"
            )
            Spacer(modifier = Modifier.width(8.dp))
            DetailsCardWithIcon(
                modifier = Modifier.weight(1f),
                iconPainter = painterResource(R.drawable.ic_streak),
                label = stringResource(R.string.current_streak),
                iconColor = if (getCurrentStreak(getCurrentDate()) > 0) MaterialTheme.colorScheme.tertiary else null,
                body = when (getCurrentStreak(getCurrentDate())) {
                    0 -> "-"
                    1 -> stringResource(R.string.day)
                    else -> stringResource(R.string.days, getCurrentStreak(getCurrentDate()).toString())
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            DetailsCardWithIcon(
                modifier = Modifier.weight(1f),
                iconPainter = painterResource(R.drawable.ic_chart),
                label = stringResource(R.string.total_effort),
                iconColor = if (totalEffort > 0f) color.mapToColor() else null,
                body = "${totalEffort.formatFloat()} ${effort.effortUnit.mapToUiText().asString()}"
            )
            Spacer(modifier = Modifier.width(8.dp))
            DetailsCardWithIcon(
                modifier = Modifier.weight(1f),
                iconPainter = painterResource(R.drawable.ic_notification),
                label = stringResource(R.string.notification),
                iconColor = if (notification is HabitNotificationSetting.Enabled) color.mapToColor() else null,
                body = if (notification is HabitNotificationSetting.Enabled) (notification as HabitNotificationSetting.Enabled).time.toString() else "-"
            )
        }
    }
}