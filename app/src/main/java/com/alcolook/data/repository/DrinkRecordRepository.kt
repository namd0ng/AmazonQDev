package com.alcolook.data.repository

import com.alcolook.data.model.DrinkRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DrinkRecordRepository {
    suspend fun insertRecord(record: DrinkRecord)
    suspend fun deleteRecord(recordId: Long)
    fun getRecordsByDate(date: LocalDate): Flow<List<DrinkRecord>>
    fun getRecordsByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DrinkRecord>>
}
