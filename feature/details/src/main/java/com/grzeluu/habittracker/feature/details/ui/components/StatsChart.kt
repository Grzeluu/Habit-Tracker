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

@Composable
fun Chart(
    data: List<Pair<Float, String>> = emptyList(),
    desiredEffort: Float,
    modifier: Modifier = Modifier,
    graphColor: Color,
) {
    val transparentColor = remember { graphColor.copy(alpha = 0.5f) }
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val maxValue = data.maxOfOrNull { it.first } ?: desiredEffort

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
        val spacePerHorizontalLabel = (size.width) / data.size

        fun getHeightGraphForCanvas(value: Float) = value + spacing
        val graphHeight = size.height - spacing * 2

        (data.indices step (data.size / 7)).forEach { i ->
            val label = data[i].second
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    spacing + i * spacePerHorizontalLabel,
                    size.height,
                    horizontalTextPaint
                )
            }
        }
        listOf(maxValue, maxValue / 2, 0f).sorted().forEachIndexed { i, value ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    value.toInt().toString(),
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

                val x1 = spacing + i * spacePerHorizontalLabel
                val y1 = getHeightGraphForCanvas(graphHeight - leftRatio * graphHeight)

                if (i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1)
                this.lineTo(
                    x1, y1
                )
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
            style = Stroke(width = 3.dp.toPx(), cap = Stroke.DefaultCap)
        )
    }
}