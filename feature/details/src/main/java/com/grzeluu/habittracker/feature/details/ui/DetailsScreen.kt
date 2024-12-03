package com.grzeluu.habittracker.feature.details.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.mapper.mapToUiText
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.feature.details.ui.components.DetailsCardWithIcon
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTitleCard
import com.grzeluu.habittracker.feature.details.ui.components.DetailsTopBar
import com.grzeluu.habittracker.feature.details.ui.event.DetailsEvent
import com.grzeluu.habittracker.feature.details.ui.state.DetailsDataState
import com.grzeluu.habittracker.util.date.getCurrentDate

@Composable
fun DetailsScreen(onNavigateBack: () -> Unit) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DetailsTopBar(
                uiState = uiState,
                onNavigateBack = onNavigateBack,
                onDelete = { viewModel.onEvent(DetailsEvent.OnDeleteHabit) },
                onEdit = { /* TODO */ },
                onArchive = { viewModel.onEvent(DetailsEvent.OnArchiveHabit) }
            )
        }
    ) { innerPadding ->
        BaseScreenContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            uiState
        ) { uiData ->
            DetailsScreenContent(uiData)
        }
    }
}

@Composable
private fun DetailsScreenContent(uiData: DetailsDataState) {
    with(uiData.habit) {
        Column(modifier = Modifier) {
            Column(
                modifier = Modifier.padding(horizontal = AppSizes.screenPadding),
                horizontalAlignment = Alignment.Start
            ) {
                DetailsTitleCard(modifier = Modifier.fillMaxWidth(), habit = uiData.habit)
                Spacer(modifier = Modifier.height(AppSizes.spaceBetweenScreenSections))
                Row {
                    DetailsCardWithIcon(
                        modifier = Modifier.weight(1f),
                        iconPainter = painterResource(R.drawable.ic_goal),
                        iconColor = color.mapToColor(),
                        label = stringResource(R.string.daily_goal),
                        body = "${effort.desiredValue} ${effort.effortUnit.mapToUiText().asString()}"
                    )
                    Spacer(modifier = Modifier.width(AppSizes.spaceBetweenScreenSections))
                    DetailsCardWithIcon(
                        modifier = Modifier.weight(1f),
                        iconPainter = painterResource(R.drawable.ic_streak),
                        label = stringResource(R.string.current_streak),
                        iconColor = if(currentStreak(getCurrentDate()) > 0) MaterialTheme.colorScheme.tertiary else null,
                        body = when (currentStreak(getCurrentDate())) {
                            0 -> "-"
                            1 -> stringResource(R.string.day)
                            else -> stringResource(R.string.days, currentStreak(getCurrentDate()).toString())
                        }
                    )
                }
            }
        }
    }
}


