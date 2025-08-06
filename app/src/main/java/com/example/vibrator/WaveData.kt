package com.example.vibrator

import androidx.compose.ui.geometry.Offset

data class WaveData(
    val recordedValues: List<Float> = emptyList(),
    val isRecording: Boolean = false,
    val isPlaying: Boolean = false,
    val circlePosition: Offset = Offset(150f, 280f) // Начальная позиция кружка
)