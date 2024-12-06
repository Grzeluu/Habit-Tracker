package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.grzeluu.habittracker.common.ui.dialog.ActionDialog
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.dialog.ActionDialogAction

@Composable
fun ConfirmArchiveHabitDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    ActionDialog(
        isVisible = isVisible,
        onDismissRequest = onDismissRequest,
        iconPainter = painterResource(R.drawable.ic_archive),
        title = stringResource(R.string.archive_habit),
        message = stringResource(R.string.archive_habit_message),
        buttonsActions = listOf(
            ActionDialogAction(
                text = stringResource(R.string.archive),
                onAction = onDeleteConfirmed
            ),
            ActionDialogAction(
                text = stringResource(R.string.cancel),
                onAction = { }
            ),
        )
    )
}