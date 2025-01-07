package com.grzeluu.habittracker.common.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.card.SimpleHabitCard
import com.grzeluu.habittracker.common.ui.mapper.MappingType
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.text.UiText
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.common.ui.theme.HabitTrackerTheme
import com.grzeluu.habittracker.util.datetime.DateFormat
import com.grzeluu.habittracker.util.datetime.format
import com.grzeluu.habittracker.util.datetime.getCurrentDate
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon
import com.grzeluu.habittracker.util.enums.EffortUnit
import com.grzeluu.habittracker.util.numbers.formatFloat
import kotlinx.datetime.LocalDate

@Composable
fun HabitEffortDialog(
    isDialogVisible: Boolean,
    currentEffort: Float,
    desiredEffortValue: Float,
    effortUnit: EffortUnit,
    habitName: String,
    habitDescription: String?,
    habitColor: CardColor,
    habitIcon: CardIcon,
    onSetProgress: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    entryDate: LocalDate? = null,
) {
    if (!isDialogVisible) return

    var progressTextValue by remember {
        mutableStateOf(
            if (currentEffort > 0) {
                currentEffort.formatFloat()
            } else {
                desiredEffortValue.formatFloat()
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
                    modifier = Modifier.fillMaxWidth(),
                    desiredEffortValue = desiredEffortValue,
                    effortUnit = effortUnit,
                    habitName = habitName,
                    habitDescription = habitDescription,
                    habitColor = habitColor,
                    habitIcon = habitIcon,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSizes.dialogInnerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    entryDate?.let {
                        Text(
                            text = stringResource(
                                R.string.set_for_the_date,
                                entryDate.format(DateFormat.DAY_MONTH_YEAR),
                            ),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CustomTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
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

                        val unitText = effortUnit.mapToUiText(MappingType.PLURAL)
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
                            onSetProgress.invoke(progressTextValue.toFloatOrNull() ?: 0f)
                            onDismissRequest()
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_checked_filled), null
                        )
                        Spacer(modifier = Modifier.width(AppSizes.spaceBetweenIconAndText))
                        Text(
                            stringResource(R.string.add_progress)
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
            habitName = "Drink water",
            entryDate = getCurrentDate(),
            isDialogVisible = true,
            currentEffort = 0f,
            habitIcon = CardIcon.DRINK,
            habitColor = CardColor.BLUE,
            habitDescription = "At least 2.5l daily",
            effortUnit = EffortUnit.LITERS,
            desiredEffortValue = 2.5f,
            onSetProgress = { _ -> },
            onDismissRequest = {}
        )
    }
}
