package com.grzeluu.habittracker.util.permissions

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat


fun Context.checkNotificationPermission(
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    onGranted: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        checkAndRequestPermission(
            POST_NOTIFICATIONS,
            launcher,
            onGranted
        )
    }
}

fun Context.checkAndRequestPermission(
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    onGranted: () -> Unit,
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(this, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        launcher.launch(permission)
    }
}