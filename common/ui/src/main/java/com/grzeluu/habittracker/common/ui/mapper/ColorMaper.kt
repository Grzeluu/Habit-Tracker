package com.grzeluu.habittracker.common.ui.mapper

import androidx.compose.ui.graphics.Color
import com.grzeluu.habittracker.util.enums.CardColor
import com.grzeluu.habittracker.common.ui.color.CardColors

fun CardColor.mapToColor(): Color =
    when(this)  {
        CardColor.YELLOW -> CardColors.YELLOW
        CardColor.GREEN -> CardColors.GREEN
        CardColor.ORANGE -> CardColors.ORANGE
        CardColor.RED -> CardColors.RED
        CardColor.PINK -> CardColors.PINK
        CardColor.BLUE -> CardColors.BLUE
        CardColor.PURPLE -> CardColors.PURPLE
        CardColor.MAGENTA ->  CardColors.MAGENTA
        CardColor.TEAL ->  CardColors.TEAL
        CardColor.BROWN ->  CardColors.BROWN
    }