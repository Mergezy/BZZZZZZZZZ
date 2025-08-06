package com.example.vibrator

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class WaveRepository {
    fun saveWaveData(context: Context, values: List<Float>) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val filename = "wave_data_$timestamp.txt"
            val content = values.joinToString("\n")

            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(content.toByteArray())
            }

            Toast.makeText(context, "Данные сохранены в $filename", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}