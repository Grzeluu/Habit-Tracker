package com.grzeluu.habittracker.common.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.padding.AppSizes

data class ActionDialogAction(
    val text: String,
    val onAction: () -> Unit,
)

@Composable
fun ActionDialog(
    isVisible: Boolean = true,
    iconPainter: Painter,
    title: String,
    message: String,
    buttonsActions: List<ActionDialogAction>? = null,
    onDismissRequest: () -> Unit
) {
    if (isVisible) {
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
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = iconPainter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title, style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message, style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        buttonsActions?.reversed()?.forEach {
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = {
                                it.onAction()
                                onDismissRequest()
                            }) {
                                Text(it.text)
                            }
                        } ?: TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ActionDialogPreview() {
    ActionDialog(iconPainter = painterResource(R.drawable.ic_delete),
        title = "Delete habit",
        message = "Are you sure you want to delete this habit?",
        buttonsActions = listOf(ActionDialogAction(text = "Delete", onAction = {}),
            ActionDialogAction(text = "Cancel", onAction = {})),
        onDismissRequest = {})
}

@Preview
@Composable
fun ActionDialogPreviewWithoutActions() {
    ActionDialog(iconPainter = painterResource(R.drawable.ic_delete),
        title = "Delete habit",
        message = "Habit has been successfully deleted.",
        onDismissRequest = {})
}