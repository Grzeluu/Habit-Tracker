package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.label.BasicLabel

@Composable
fun SetNotificationsView(
    isNotificationsEnabled: Boolean,
    onNotificationsEnabledChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicLabel(text = stringResource(R.string.get_notifications))
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isNotificationsEnabled,
            onCheckedChange = onNotificationsEnabledChange
        )
    }
}