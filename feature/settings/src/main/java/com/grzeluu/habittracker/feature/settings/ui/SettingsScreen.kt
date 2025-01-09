package com.grzeluu.habittracker.feature.settings.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grzeluu.habittracker.base.ui.BaseScreenContainer
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.padding.AppSizes
import com.grzeluu.habittracker.common.ui.permission.NotificationPermissionRationale
import com.grzeluu.habittracker.common.ui.permission.permissionLauncher
import com.grzeluu.habittracker.feature.settings.components.SettingsLine
import com.grzeluu.habittracker.feature.settings.components.SettingsRow
import com.grzeluu.habittracker.feature.settings.event.SettingsEvent
import com.grzeluu.habittracker.util.permissions.checkNotificationPermission

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val context = LocalContext.current
    var isNotificationPermissionDialogVisible by remember { mutableStateOf(false) }
    val launcher = permissionLauncher(
        onPermissionGranted = { viewModel.onEvent(SettingsEvent.OnChangeNotifications(true)) },
        onPermissionDenied = { isNotificationPermissionDialogVisible = true }
    )

    BaseScreenContainer(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSizes.screenPadding),
        uiState = uiState,
    ) { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            NotificationPermissionRationale(
                isVisible = isNotificationPermissionDialogVisible,
                onDismissRequest = { isNotificationPermissionDialogVisible = false }
            )
            SettingsRow(
                icon = painterResource(R.drawable.ic_notification),
                title = stringResource(R.string.notifications),
                onClick = { viewModel.onEvent(SettingsEvent.OnChangeNotifications(!data.isNotificationsEnabled)) },
                trailingElement = {
                    Switch(
                        modifier = Modifier.height(20.dp),
                        checked = data.isNotificationsEnabled,
                        onCheckedChange = {
                            if (it) {
                                context.checkNotificationPermission(launcher) {
                                    viewModel.onEvent(SettingsEvent.OnChangeNotifications(true))
                                }
                            } else {
                                viewModel.onEvent(SettingsEvent.OnChangeNotifications(false))
                            }
                        }
                    )
                }
            )
            SettingsLine()
            SettingsRow(
                icon = painterResource(R.drawable.ic_ui_mode),
                title = stringResource(R.string.dark_mode),
                onClick = {
                    viewModel.onEvent(
                        SettingsEvent.OnChangeDarkMode(!(data.isDarkModeEnabled ?: isSystemInDarkTheme))
                    )
                },
                trailingElement = {
                    Switch(
                        modifier = Modifier.height(20.dp),
                        checked = data.isDarkModeEnabled ?: isSystemInDarkTheme,
                        onCheckedChange = { viewModel.onEvent(SettingsEvent.OnChangeDarkMode(it)) }
                    )
                }
            )
            SettingsLine()
            SettingsRow(
                icon = painterResource(R.drawable.ic_info),
                title = stringResource(R.string.about_app),
                onClick = { /*TODO*/ },
                trailingElement = {
                    Icon(painter = painterResource(R.drawable.ic_arrow_right), null)
                }
            )
            SettingsLine()
            SettingsRow(
                icon = painterResource(R.drawable.ic_info),
                title = stringResource(R.string.privacy_policy),
                onClick = { /*TODO*/ },
                trailingElement = {
                    Icon(painter = painterResource(R.drawable.ic_arrow_right), null)
                }
            )
        }
    }
}