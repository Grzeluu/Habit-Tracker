package com.grzeluu.habittracker.feature.addhabit.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.util.enums.CardIcon

@Composable
fun ColorSelectionRow(
    selectedColor: CardColor,
    onSelectionChanged: (CardColor) -> Unit
) {
    LazyRow {
        items(CardColor.entries) { color ->
            ColorCircle(
                modifier = Modifier.padding(end = 12.dp),
                onClicked = onSelectionChanged,
                isSelected = color == selectedColor,
                size = 42.dp,
                cardColor = color,
            )
        }
    }
}