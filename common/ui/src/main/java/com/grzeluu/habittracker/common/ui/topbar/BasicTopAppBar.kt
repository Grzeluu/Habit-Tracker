package com.grzeluu.habittracker.common.ui.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.grzeluu.habittracker.common.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigateBack: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { title?.let { Text(text = title) } },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(painterResource(R.drawable.ic_back), contentDescription = stringResource(R.string.navigate_back))
            }
        }
    )
}