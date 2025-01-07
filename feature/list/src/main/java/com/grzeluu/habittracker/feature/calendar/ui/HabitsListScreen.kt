package com.grzeluu.habittracker.feature.calendar.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.calendar.ui.components.HabitsListCard

@Composable
fun HabitsListScreen(
    onNavigateToDetails: (habitId: Long) -> Unit
) {
    val viewModel: HabitsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    BaseScreenContainer(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSizes.screenPadding),
        uiState = uiState,
    ) { data ->
        LazyColumn {
            item { BasicLabel(text = stringResource(R.string.active_habits)) }
            item { Spacer(modifier = Modifier.height(4.dp)) }
            itemsIndexed(data.activeHabits) { index, habit ->
                HabitsListCard(habit) {
                    onNavigateToDetails(habit.id)
                }
                if (index != data.activeHabits.lastIndex) {
                    Spacer(modifier = Modifier.height(AppSizes.spaceBetweenCards))
                }
            }
            if (data.archivedHabits.isNotEmpty()) {
                item { Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections)) }
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { BasicLabel(text = stringResource(R.string.archived_habits)) }
                itemsIndexed(data.archivedHabits) { index, habit ->
                    HabitsListCard(habit) {
                        onNavigateToDetails(habit.id)
                    }
                    if (index != data.activeHabits.lastIndex) {
                        Spacer(modifier = Modifier.height(AppSizes.spaceBetweenCards))
                    }
                }
            }
        }
    }
}

