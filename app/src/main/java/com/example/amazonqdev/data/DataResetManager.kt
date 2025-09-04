package com.example.amazonqdev.data

import android.content.Context
import android.content.SharedPreferences

class DataResetManager(private val context: Context) {
    
    fun resetAllData(): Boolean {
        return try {
            // Clear SharedPreferences
            val sharedPrefs = context.getSharedPreferences("alcolook_prefs", Context.MODE_PRIVATE)
            sharedPrefs.edit().clear().apply()
            
            // Clear app cache
            context.cacheDir.deleteRecursively()
            
            // Clear internal files
            context.filesDir.listFiles()?.forEach { file ->
                if (file.name.endsWith(".json") || file.name.contains("backup")) {
                    file.delete()
                }
            }
            
            true
        } catch (e: Exception) {
            false
        }
    }
}