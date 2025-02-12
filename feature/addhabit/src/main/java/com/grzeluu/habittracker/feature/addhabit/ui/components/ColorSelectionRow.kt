package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.util.enums.CardColor

@Composable
fun ColorSelectionRow(
    selectedColor: CardColor,
    onSelectionChanged: (CardColor) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(selectedColor) {
        val index = CardColor.entries.indexOf(selectedColor)
        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
        if (index > 0) {
            itemInfo?.let {
                val center = lazyListState.layoutInfo.viewportEndOffset / 2
                val childCenter = itemInfo.offset + itemInfo.size / 2
                lazyListState.animateScrollBy((childCenter - center).toFloat())
            } ?: lazyListState.animateScrollToItem(index)
        }
    }
    LazyRow(state = lazyListState) {
        items(CardColor.entries) { color ->
            ColorCircle(
                modifier = Modifier.padding(horizontal = 6.dp),
                onClicked = onSelectionChanged,
                isSelected = color == selectedColor,
                size = 42.dp,
                cardColor = color,
            )
        }
    }
}