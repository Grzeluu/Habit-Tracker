package com.grzeluu.habittracker.common.ui.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.util.toPresentationTime

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    name: String,
    iconPainter: Painter,
    color: Color,
    description: String?,
    desiredTime: Int?,
    timeSpent: Int?,
    isDone: Boolean,
    onButtonClicked: () -> Unit
) {

    var filled = 1f
    var timeString: String? = null

    if (desiredTime == null || timeSpent == null) {
        filled = 1f
    } else {
        filled = timeSpent.toFloat() / desiredTime.toFloat()
        timeString = "${timeSpent.toPresentationTime()} / ${desiredTime.toPresentationTime()}"
    }

    Card(modifier = modifier.wrapContentHeight()) {
        Box(modifier.fillMaxWidth()) {
            FilledBackground(
                modifier = Modifier.fillMaxWidth(),
                color = color.copy(alpha = 0.4f),
                fill = filled
            )
            Row(
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    painter = iconPainter,
                    contentDescription = null,
                    tint = color.copy(alpha = 0.9f),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = description ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                timeString?.let {
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = timeString,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                IconButton(
                    modifier = Modifier.size(42.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    onClick = onButtonClicked,
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(if (isDone) R.drawable.ic_checked_filled else R.drawable.ic_checked),
                        contentDescription = stringResource(R.string.done),
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun HabitCardPreviewDone() {
    HabitCard(
        modifier = Modifier.wrapContentHeight(),
        name = "Running",
        iconPainter = painterResource(R.drawable.ic_calendar),
        color = Color.Yellow,
        description = "15km - moderate pace",
        desiredTime = 30,
        timeSpent = 30,
        isDone = true,
        onButtonClicked = {}
    )
}

@Preview
@Composable
fun HabitCardPreviewAlmostDone() {
    HabitCard(
        modifier = Modifier.wrapContentHeight(),
        name = "Running",
        iconPainter = painterResource(R.drawable.ic_calendar),
        color = Color.Red,
        description = "15km - moderate pace",
        desiredTime = 30,
        timeSpent = 20,
        isDone = true,
        onButtonClicked = {}
    )
}

@Preview
@Composable
fun HabitCardUndone() {
    HabitCard(
        modifier = Modifier.wrapContentHeight(),
        name = "Reading",
        iconPainter = painterResource(R.drawable.ic_home),
        color = Color.Green,
        description = "30 pages",
        desiredTime = 30,
        timeSpent = 0,
        isDone = false,
        onButtonClicked = {}
    )
}