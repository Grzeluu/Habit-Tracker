package com.grzeluu.habittracker.common.ui.permission

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.dialog.ActionDialog
import com.grzeluu.habittracker.common.ui.dialog.ActionDialogAction

@Composable
fun NotificationPermissionRationale(
    isVisible: Boolean,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val permissionIntent = remember {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }
    }

    ActionDialog(
        isVisible = isVisible,
        onDismissRequest = onDismissRequest,
        iconPainter = painterResource(R.drawable.ic_notification),
        title = stringResource(R.string.permission_notification_title),
        message = stringResource(R.string.permission_notification_message),
        buttonsActions = listOf(
            ActionDialogAction(
                text = stringResource(R.string.open_settings),
                onAction = {
                    context.startActivity(permissionIntent)
                    onDismissRequest()
                }
            ),
            ActionDialogAction(
                text = stringResource(R.string.deny),
                onAction = onDismissRequest
            ),
        )
    )
}