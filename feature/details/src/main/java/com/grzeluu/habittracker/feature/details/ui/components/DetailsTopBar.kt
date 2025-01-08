package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.base.ui.state.UiState
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.common.ui.topbar.BasicTopAppBar
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.feature.details.ui.state.DetailsDataState

@Composable
fun DetailsTopBar(
    uiState: UiState<DetailsDataState>,
    onNavigateBack: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onArchive: () -> Unit,
) {
    BasicTopAppBar(
        title = null,
        onNavigateBack = onNavigateBack,
        actions = {
            val habit = (uiState as? UiState.Success)?.data?.habit ?: return@BasicTopAppBar
            val buttonColors = IconButtonDefaults.iconButtonColors(
                contentColor = habit.color.mapToColor().copy(alpha = 0.9f)
            )
            if (!habit.isArchive) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = buttonColors,
                    onClick = onEdit,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = stringResource(R.string.edit_habit),
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                colors = buttonColors,
                onClick = onDelete,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete_habit),
                )
            }
            if (!habit.isArchive) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = buttonColors,
                    onClick = onArchive,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_archive),
                        contentDescription = stringResource(R.string.archive_habit),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    )
}