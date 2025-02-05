package com.grzeluu.habittracker.base.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grzeluu.habittracker.base.ui.state.UiState
import com.grzeluu.habittracker.common.ui.error.GenericErrorView
import com.grzeluu.habittracker.common.ui.loading.LoadingView

@Composable
fun <DATA> UiStateScreenContainer(
    modifier: Modifier = Modifier,
    uiState: UiState<DATA>,
    content: @Composable (DATA) -> Unit
) {
    when (uiState) {
        UiState.Loading -> LoadingView(modifier)
        is UiState.Failure -> GenericErrorView(modifier)
        is UiState.Success -> Box(modifier) { content(uiState.data) }
    }
}