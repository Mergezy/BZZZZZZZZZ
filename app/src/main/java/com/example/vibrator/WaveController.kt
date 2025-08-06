package com.example.vibrator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun WaveController(
    waveRepository: WaveRepository = WaveRepository()
) {
    val context = LocalContext.current
    var waveData by remember { mutableStateOf(WaveData()) }
    val controlAreaSize = 300.dp
    val circleRadius = 15f

    // Получаем density в composable контексте
    val density = LocalDensity.current
    val controlAreaHeightPx = remember(density) {
        with(density) { controlAreaSize.toPx() }
    }

    // Автоматическое добавление значений при записи
    LaunchedEffect(waveData.isRecording) {
        while (waveData.isRecording) {
            val normalizedY = 1f - (waveData.circlePosition.y - circleRadius) /
                    (controlAreaHeightPx - 2 * circleRadius)
            waveData = waveData.copy(
                recordedValues = waveData.recordedValues + normalizedY.coerceIn(0f, 1f)
            )
            delay(30) // ~50 FPS
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // График вибрации
        InfiniteWave(
            values = waveData.recordedValues,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Black.copy(alpha = 0.05f)),
            isPlaying = waveData.isPlaying
        )
        // Область управления (графический редактор)
        Box(
            modifier = Modifier
                .size(controlAreaSize)
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val newPosition = change.position
                        val boundedX = newPosition.x.coerceIn(
                            circleRadius,
                            size.width - circleRadius
                        )
                        val boundedY = newPosition.y.coerceIn(
                            circleRadius,
                            size.height - circleRadius
                        )
                        waveData = waveData.copy(
                            circlePosition = Offset(boundedX, boundedY)
                        )
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Основной Canvas для управления
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Отрисовка кружка управления
                drawCircle(
                    color = Color.Blue,
                    center = waveData.circlePosition,
                    radius = circleRadius
                )
            }

            // Кнопка записи (красный кружок)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd) // или любое другое выравнивание
                    .offset(x = (-13).dp, y = 20.dp) // корректировка позиции
                    .size(25.dp) // область нажатия
                    .clickable {
                        waveData = waveData.copy(isRecording = !waveData.isRecording)
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = if (waveData.isRecording) Color.Red else Color.Blue,
                        radius = 20f
                    )
                }
            }
        }

        WaveControlPanel(
            waveData = waveData,
            onWaveDataChanged = { newData -> waveData = newData },
            onSave = { waveRepository.saveWaveData(context, waveData.recordedValues) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}