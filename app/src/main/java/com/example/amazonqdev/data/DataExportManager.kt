package com.example.amazonqdev.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataExportManager {
    fun createExportIntent(): Intent {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
        val fileName = "AlcoLook-backup-$timestamp.json"
        
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
    }
    
    fun generateBackupData(
        gender: String,
        age: String,
        goal: String,
        theme: String
    ): String {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"
        
        return """
{
  "exported_at": "$timestamp",
  "app_version": "1.0.0",
  "profile": {
    "gender": "${when(gender) { "남성" -> "male"; "여성" -> "female"; else -> "" }}",
    "age": "$age",
    "weeklyGoal": "${when(goal) { "7잔(권장)" -> "7"; "14잔(저위험)" -> "14"; "21잔(최대)" -> "21"; else -> "14" }}"
  },
  "settings": {
    "notifications": true,
    "healthDataConsent": false,
    "theme": "${when(theme) { "시스템" -> "system"; "다크" -> "dark"; "라이트" -> "light"; else -> "system" }}"
  },
  "drink_records": [
    {
      "id": "1",
      "date": "2025-01-15",
      "type": "맥주",
      "quantity": 1,
      "amount": 500,
      "unitAmount": 500,
      "abv": 4.5,
      "notes": "친구들과 회식",
      "analysisResult": {
        "probability": 75,
        "confidence": 85,
        "advice": "얼굴에 음주 흔적이 보입니다."
      }
    }
  ],
  "total_records": 1
}
        """.trimIndent()
    }
    
    fun writeToUri(context: Context, uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(content.toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}