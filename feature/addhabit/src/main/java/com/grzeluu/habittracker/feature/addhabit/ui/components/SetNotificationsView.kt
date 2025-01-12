package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel
import com.grzeluu.habittracker.common.ui.textfield.CustomTextField
import com.grzeluu.habittracker.feature.addhabit.ui.state.NotificationSettings

@Composable
fun SetNotificationsView(
    notificationSettings: NotificationSettings,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onShowTimePicker: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicLabel(text = stringResource(R.string.get_notifications))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = notificationSettings.isEnabled,
                onCheckedChange = {
                    onNotificationsEnabledChange(it)
                }
            )
        }
        AnimatedVisibility(
            visible = notificationSettings.isEnabled,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    label = stringResource(R.string.notification_time),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        onShowTimePicker()
                                    }
                                }
                            }
                        },
                    value = notificationSettings.time.toString(),
                    onValueChange = {},
                    withClearTextOption = false,
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_time),
                            contentDescription = stringResource(R.string.set_time)
                        )
                    },
                )
            }
        }
    }
}