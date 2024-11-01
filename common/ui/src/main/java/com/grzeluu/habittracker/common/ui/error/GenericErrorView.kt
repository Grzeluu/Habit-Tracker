package com.grzeluu.habittracker.common.ui.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.R
import com.grzeluu.habittracker.common.ui.text.UiText

@Composable
fun GenericErrorView(
    modifier: Modifier = Modifier,
    errorText: UiText = UiText.StringResource(R.string.something_has_gone_wrong),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(0.7f),
                painter = painterResource(id = R.drawable.error),
                contentDescription = null,

                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorText.asString(),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}