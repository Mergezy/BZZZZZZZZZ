package com.example.vibrator

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InfiniteWave(
    values: List<Float>,
    modifier: Modifier = Modifier,
    waveColor: Color = Color.Blue,
    lineWidth: Dp = 2.dp,
    amplitude: Float = 100f,
    isPlaying: Boolean = false
) {
    val density = LocalDensity.current
    val lineWidthPx = with(density) { lineWidth.toPx() }

    val visibleValues = remember(values, isPlaying) {
        if (isPlaying) values else values.takeLast(100)
    }

    Canvas(modifier = modifier.background(Color.Black.copy(alpha = 0.05f))) {
        if (visibleValues.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val centerY = height / 2

        // Границы амплитуды
        drawAmplitudeBounds(centerY, amplitude, width)

        // Линия графика
        drawWavePath(visibleValues, width, centerY, amplitude, waveColor, lineWidthPx)
    }
}

private fun DrawScope.drawAmplitudeBounds(centerY: Float, amplitude: Float, width: Float) {
    drawLine(
        color = Color.Gray.copy(alpha = 0.3f),
        start = Offset(0f, centerY - amplitude),
        end = Offset(width, centerY - amplitude),
        strokeWidth = 1.dp.toPx()
    )
    drawLine(
        color = Color.Gray.copy(alpha = 0.3f),
        start = Offset(0f, centerY + amplitude),
        end = Offset(width, centerY + amplitude),
        strokeWidth = 1.dp.toPx()
    )
}

private fun DrawScope.drawWavePath(
    values: List<Float>,
    width: Float,
    centerY: Float,
    amplitude: Float,
    color: Color,
    strokeWidth: Float
) {
    val path = Path().apply {
        if (values.isEmpty()) return@apply

        moveTo(0f, centerY - values.first() * amplitude)

        values.forEachIndexed { index, y ->
            val x = index.toFloat() / values.size.coerceAtLeast(1) * width
            val waveY = centerY - y * amplitude
            if (index == 0) {
                moveTo(x, waveY)
            } else {
                lineTo(x, waveY)
            }
        }
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}