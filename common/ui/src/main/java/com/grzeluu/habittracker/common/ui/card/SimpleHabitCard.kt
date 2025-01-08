package com.grzeluu.habittracker.common.ui.card

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
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.numbers.formatFloat

@Composable
fun SimpleHabitCard(
    modifier: Modifier = Modifier,
    desiredEffortValue: Float,
    effortUnit: EffortUnit,
    habitName: String,
    habitDescription: String?,
    habitColor: CardColor,
    habitIcon: CardIcon,
    isArchive: Boolean = false,
    onCardClicked: (() -> Unit)? = null,
) {
    val effortString =
        buildString {
            append(desiredEffortValue.formatFloat())
            append(" ")
            append(effortUnit.mapToUiText().asString())
        }

    Card(
        onClick = { onCardClicked?.invoke() },
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isArchive) MaterialTheme.colorScheme.surfaceVariant
            else habitColor.mapToColor().copy(alpha = 0.25f),
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
                painter = painterResource(habitIcon.mapToDrawableRes()),
                contentDescription = null,
                tint = habitColor.mapToColor(),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = habitName,
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis
                )
                if (!habitDescription.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = habitDescription,
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
            desiredEffortValue = 2.5f,
            effortUnit = EffortUnit.LITERS,
            habitName = "Drink water",
            habitDescription = "At least 2.5l daily",
            habitColor = CardColor.BLUE,
            habitIcon = CardIcon.DRINK
        )
    }
}