package com.grzeluu.habittracker.feature.details.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grzeluu.habittracker.util.numbers.shortFormatFloat
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun Chart(
    data: List<Pair<Float, String>> = emptyList(),
    desiredEffort: Float,
    modifier: Modifier = Modifier,
    graphColor: Color,
) {
    if (data.isEmpty()) return

    val transparentColor = remember { graphColor.copy(alpha = 0f) }
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val maxValue = maxOf(data.maxOfOrNull { it.first } ?: 0f, desiredEffort)

    val spacing = 80f
    val density = LocalDensity.current

    val verticalTextPaint = remember {
        Paint().apply {
            color = onSurfaceColor.toArgb()
            textSize = density.run { 14.sp.toPx() }
            textAlign = Paint.Align.LEFT
        }
    }

    val horizontalTextPaint = remember {
        Paint().apply {
            color = onSurfaceColor.toArgb()
            textSize = density.run { 14.sp.toPx() }
            textAlign = Paint.Align.CENTER
        }
    }
    Canvas(modifier = modifier) {
        val spacePerHorizontalItem = (size.width)/ data.size
        val strokeWidth = 3.dp.toPx()

        val graphHeight = size.height - spacing * 2
        fun getHeightGraphForCanvas(value: Float): Float {
            if (value == graphHeight) {
                return (graphHeight - strokeWidth / 2) + spacing
            }
            return value + spacing
        }

        val stepSize = ceil((data.size / 7f)).toInt()
        val indicesStep = if (stepSize > 0) stepSize else 1
        (data.indices step indicesStep).forEach { i ->
            val label = data[i].second
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    spacing + i * spacePerHorizontalItem,
                    size.height,
                    horizontalTextPaint
                )
            }
        }
        listOf(maxValue, maxValue / 2, 0f).sorted().forEachIndexed { i, value ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    value.shortFormatFloat(),
                    12f,
                    getHeightGraphForCanvas(graphHeight - i * graphHeight / 2),
                    verticalTextPaint
                )
            }
        }

        var lastX = 0f
        val strokePath = Path().apply {
            for (i in data.indices) {
                val currentData = data[i]
                val nextData = data.getOrNull(i + 1) ?: data.last()

                val leftRatio = currentData.first / maxValue
                val rightRatio = nextData.first / maxValue

                val x1 = spacing + i * spacePerHorizontalItem
                val y1 = getHeightGraphForCanvas(graphHeight - leftRatio * graphHeight)
                val x2 = spacing + (i + 1) * spacePerHorizontalItem
                val y2 = getHeightGraphForCanvas(graphHeight - rightRatio * graphHeight)

                if (i == 0) {
                    moveTo(x1, y1)
                }

                val controlX1 = x1 + (x2 - x1) / 3
                val controlX2 = x1 + 2 * (x2 - x1) / 3

                lastX = x1

                if (i != data.lastIndex) {
                    cubicTo(
                        controlX1, y1,
                        controlX2, y2,
                        x2, y2
                    )
                }
            }
        }
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }
        drawPath(
            fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(graphColor, transparentColor),
                endY = size.height - spacing
            ),
        )
        drawPath(
            strokePath,
            color = graphColor,
            style = Stroke(strokeWidth, cap = Stroke.DefaultCap)
        )
    }
}