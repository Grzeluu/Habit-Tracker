package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.R
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    isVisible: Boolean,
    selectedTime: LocalTime,
    onDismissRequest: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
) {
    if (isVisible) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
            is24Hour = true,
        )

        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier.padding(AppSizes.dialogPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicLabel(
                        modifier = Modifier.align(Alignment.Start),
                        text = stringResource(R.string.notification_time)
                    )
                    Spacer(modifier = Modifier.padding(AppSizes.spaceBetweenFormElements))
                    TimeInput(
                        state = timePickerState,
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismissRequest) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(onClick = {
                            onTimeSelected(LocalTime(timePickerState.hour, timePickerState.minute))
                            onDismissRequest()
                        }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            }
        }
    }
}