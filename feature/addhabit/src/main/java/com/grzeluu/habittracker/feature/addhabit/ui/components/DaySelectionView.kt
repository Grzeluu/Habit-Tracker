package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.util.enums.Day

@Composable
fun DaySelectionView(
    isError: Boolean = false,
    supportingText: String,
    onDayCheckedChange: (Day, Boolean) -> Unit,
    selectedDays: List<Day>,
    toggleSelectAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicLabel(text = stringResource(R.string.repeat_everyday))
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = selectedDays.containsAll(Day.entries),
                onCheckedChange = { toggleSelectAll() }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Day.entries.forEachIndexed { index, day ->
                DayToggleButton(
                    modifier = Modifier
                        .weight(1f),
                    onCheckedChange = { onDayCheckedChange(day, it) },
                    isChecked = selectedDays.contains(day),
                    day = day,
                    isError = isError
                )
                if (index != Day.entries.lastIndex) Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = supportingText,
            style = MaterialTheme.typography.bodySmall,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}