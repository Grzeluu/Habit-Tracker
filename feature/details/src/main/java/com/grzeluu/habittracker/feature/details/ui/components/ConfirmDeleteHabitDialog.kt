package com.grzeluu.habittracker.feature.details.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.grzeluu.habittracker.common.ui.dialog.ActionDialog
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.dialog.ActionDialogAction

@Composable
fun ConfirmDeleteHabitDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    ActionDialog(
        isVisible = isVisible,
        onDismissRequest = onDismissRequest,
        iconPainter = painterResource(R.drawable.ic_delete),
        title = stringResource(R.string.delete_habit),
        message = stringResource(R.string.delete_habit_message),
        buttonsActions = listOf(
            ActionDialogAction(
                text = stringResource(R.string.delete),
                onAction = onDeleteConfirmed
            ),
            ActionDialogAction(
                text = stringResource(R.string.cancel),
                onAction = { }
            ),
        )
    )
}