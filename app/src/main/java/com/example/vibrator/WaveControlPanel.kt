package com.example.vibrator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaveControlPanel(
    waveData: WaveData,
    onWaveDataChanged: (WaveData) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                onWaveDataChanged(waveData.copy(isRecording = !waveData.isRecording))
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(if (waveData.isRecording) "Стоп" else "Запись")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = {
                onWaveDataChanged(waveData.copy(isPlaying = !waveData.isPlaying))
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(if (waveData.isPlaying) "Пауза" else "Воспроизвести")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f)
        ) {
            Text("Сохранить")
        }
    }
}