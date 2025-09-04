package com.alcolook.data.repository

import com.alcolook.data.model.DrinkRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class DrinkRecordRepositoryImpl : DrinkRecordRepository {
    
    private val records = MutableStateFlow<List<DrinkRecord>>(emptyList())
    private var nextId = 1L
    
    override suspend fun insertRecord(record: DrinkRecord) {
        val newRecord = record.copy(id = nextId++)
        records.value = records.value + newRecord
    }
    
    override suspend fun deleteRecord(recordId: Long) {
        records.value = records.value.filter { it.id != recordId }
    }
    
    override fun getRecordsByDate(date: LocalDate): Flow<List<DrinkRecord>> {
        return records.map { list -> list.filter { it.date == date } }
    }
    
    override fun getRecordsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DrinkRecord>> {
        return records.map { list -> 
            list.filter { it.date >= startDate && it.date <= endDate }
        }
    }
    
    companion object {
        @Volatile
        private var INSTANCE: DrinkRecordRepositoryImpl? = null
        
        fun getInstance(): DrinkRecordRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DrinkRecordRepositoryImpl().also { INSTANCE = it }
            }
        }
    }
}
