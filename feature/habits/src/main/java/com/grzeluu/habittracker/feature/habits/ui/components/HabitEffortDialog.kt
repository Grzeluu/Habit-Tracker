package com.grzeluu.habittracker.feature.habits.ui.components

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.mapper.MappingType
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
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
fun HabitEffortDialog(
    dailyHabitInfo: DailyHabitInfo?,
    onSetProgress: (DailyHabitInfo, Float) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (dailyHabitInfo == null) return

    var progressTextValue by remember {
        mutableStateOf(
            if (dailyHabitInfo.currentEffort > 0) {
                dailyHabitInfo.currentEffort.formatFloat()
            } else {
                dailyHabitInfo.effort.desiredValue.formatFloat()
            }
        )
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier.padding(AppSizes.dialogPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                SimpleHabitCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(ambientColor = dailyHabitInfo.color.mapToColor(), elevation = 4.dp),
                    habitInfo = dailyHabitInfo,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSizes.dialogInnerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomTextField(
                            modifier = Modifier.weight(1f).padding(16.dp),
                            value = progressTextValue,
                            onValueChange = {
                                if (it.isEmpty() || (it.toFloatOrNull() != null && it.toFloat() >= 0f)) {
                                    progressTextValue = it
                                }
                            },
                            withClearTextOption = false,
                            alignment = TextAlign.End,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )

                        val unitText = dailyHabitInfo.effort.effortUnit.mapToUiText(MappingType.PLURAL)
                        if (unitText !is UiText.Empty) {
                            Text(
                                text = "✕",
                                style = MaterialTheme.typography.bodySmall

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.padding(end = 16.dp),
                                text = unitText.asString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSetProgress.invoke(dailyHabitInfo, progressTextValue.toFloat())
                            onDismissRequest()
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_checked_filled), null
                        )
                        Spacer(modifier = Modifier.width(AppSizes.spaceBetweenIconAndText))
                        Text(
                            stringResource( R.string.add_progress)
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun HabitEffortDialogPreview() {
    HabitTrackerTheme(
        darkTheme = true
    ) {
        HabitEffortDialog(
            dailyHabitInfo = DailyHabitInfo(
                name = "Running",
                icon = CardIcon.RUN,
                color = CardColor.RED,
                description = "Moderate pace",
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
            onSetProgress = { _, _ -> },
            onDismissRequest = {}
        )
    }
}
