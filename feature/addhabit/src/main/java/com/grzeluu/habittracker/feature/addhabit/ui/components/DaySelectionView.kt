package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.util.enums.Day

@Composable
fun DaySelectionView(
    onDayCheckedChange: (Day, Boolean) -> Unit,
    selectedDays: List<Day>,
    toggleSelectAll: () -> Unit
) {
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
    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenFormElements))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Day.entries.forEachIndexed { index, day ->
            DayToggleButton(
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)),
                onCheckedChange = { onDayCheckedChange(day, it) },
                isChecked = selectedDays.contains(day),
                day = day
            )
            if(index != Day.entries.lastIndex) Spacer(modifier = Modifier.width(4.dp))
        }
    }
}