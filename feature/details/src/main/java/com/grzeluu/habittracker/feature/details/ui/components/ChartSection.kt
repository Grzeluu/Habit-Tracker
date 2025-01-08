package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.component.habit.domain.model.Habit
import com.grzeluu.habittracker.feature.details.ui.enum.ProgressPeriod

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChartSection(
    habit: Habit,
    onSelectPeriod: (ProgressPeriod) -> Unit,
    selectedPeriod: ProgressPeriod,
    periodStats: List<Pair<Float, String>>,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        ProgressPeriod.entries.forEachIndexed { index, period ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = ProgressPeriod.entries.size),
                onClick = { onSelectPeriod(period) },
                selected = period == selectedPeriod,
                border = BorderStroke(1.dp, color = habit.color.mapToColor().copy(alpha = 0.5f)),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = habit.color.mapToColor().copy(alpha = 0.5f),
                    activeContentColor = MaterialTheme.colorScheme.onSurface,
                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                icon = {}
            ) {
                Text(stringResource(period.label))
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
    ) {
        Chart(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSizes.cardInnerPadding),
            data = periodStats,
            desiredEffort = habit.effort.desiredValue,
            graphColor = habit.color.mapToColor()
        )
    }
}