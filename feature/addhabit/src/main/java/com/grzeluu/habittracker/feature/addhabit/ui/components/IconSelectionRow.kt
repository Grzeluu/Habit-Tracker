package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.common.ui.mapper.mapToColor
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon

@Composable
fun IconSelectionRow(
    iconsColor: CardColor,
    selectedIcon: CardIcon,
    onSelectionChanged: (CardIcon) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(selectedIcon) {
        val index = CardIcon.entries.indexOf(selectedIcon)
        val itemInfo = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
        if (itemInfo != null) {
            val center = lazyListState.layoutInfo.viewportEndOffset / 2
            val childCenter = itemInfo.offset + itemInfo.size / 2
            lazyListState.animateScrollBy((childCenter - center).toFloat())
        }
    }
    LazyRow(state = lazyListState) {
        items(CardIcon.entries) { icon ->
            val animatedColor = animateColorAsState(
                targetValue = if (icon == selectedIcon) iconsColor.mapToColor() else MaterialTheme.colorScheme.outline,
                label = "AnimatedIconColor"
            )
            IconCircle(
                modifier = Modifier.padding(end = 12.dp),
                onClicked = onSelectionChanged,
                size = 42.dp,
                color = animatedColor.value,
                icon = icon
            )
        }
    }
}