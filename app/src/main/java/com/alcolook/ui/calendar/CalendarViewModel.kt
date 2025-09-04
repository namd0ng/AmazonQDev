package com.alcolook.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcolook.data.model.*
import com.alcolook.data.repository.DrinkRecordRepositoryImpl
import com.alcolook.data.repository.UserProfileRepositoryImpl
import com.alcolook.domain.AlcoholCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {

    private val drinkRecordRepository = DrinkRecordRepositoryImpl.getInstance()
    private val userProfileRepository = UserProfileRepositoryImpl.getInstance()

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _showAddDrinkDialog = MutableStateFlow(false)
    val showAddDrinkDialog: StateFlow<Boolean> = _showAddDrinkDialog.asStateFlow()

    private val _drinkingAssessment = MutableStateFlow<DrinkingAssessment?>(null)
    val drinkingAssessment: StateFlow<DrinkingAssessment?> = _drinkingAssessment.asStateFlow()

    init {
        loadMonthlyRecords()
        loadSelectedDateRecords()
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        loadSelectedDateRecords()
    }

    fun changeMonth(yearMonth: YearMonth) {
        _currentMonth.value = yearMonth
        loadMonthlyRecords()
    }

    fun showAddDrinkDialog() {
        _showAddDrinkDialog.value = true
    }

    fun hideAddDrinkDialog() {
        _showAddDrinkDialog.value = false
    }

    fun addDrinkRecord(drinkType: DrinkType, unit: String, quantity: Int, abv: Float?, memo: String?) {
        viewModelScope.launch {
            val volumeMl = AlcoholCalculator.calculateVolumeFromUnit(drinkType, unit, quantity)
            
            val record = DrinkRecord(
                date = _selectedDate.value,
                type = drinkType,
                unit = unit,
                quantity = quantity,
                abv = abv,
                volumeMl = volumeMl,
                note = memo,
                analysisProb = null
            )
            
            drinkRecordRepository.insertRecord(record)
            
            // 음주 평가 수행
            assessDrinking()
            
            hideAddDrinkDialog()
            loadSelectedDateRecords()
            loadMonthlyRecords()
        }
    }

    private fun assessDrinking() {
        viewModelScope.launch {
            val userProfile = userProfileRepository.getUserProfile().first()
            val today = _selectedDate.value
            
            // 일일 알코올 섭취량
            val dailyRecords = drinkRecordRepository.getRecordsByDate(today).first()
            val dailyAlcohol = dailyRecords.sumOf { record ->
                AlcoholCalculator.calculateAlcoholGrams(
                    record.type, 
                    record.volumeMl ?: 0, 
                    record.abv
                ).toDouble()
            }.toFloat()
            
            // 주간 알코올 섭취량
            val weekStart = today.minusDays(6)
            val weeklyRecords = drinkRecordRepository.getRecordsByDateRange(weekStart, today).first()
            val weeklyAlcohol = weeklyRecords.sumOf { record ->
                AlcoholCalculator.calculateAlcoholGrams(
                    record.type,
                    record.volumeMl ?: 0,
                    record.abv
                ).toDouble()
            }.toFloat()
            
            // 월간 폭음 횟수 (간단히 일일 70g 이상인 날 계산)
            val monthStart = today.withDayOfMonth(1)
            val monthlyRecords = drinkRecordRepository.getRecordsByDateRange(monthStart, today).first()
            val dailyTotals = monthlyRecords.groupBy { it.date }
                .mapValues { (_, records) ->
                    records.sumOf { record ->
                        AlcoholCalculator.calculateAlcoholGrams(
                            record.type,
                            record.volumeMl ?: 0,
                            record.abv
                        ).toDouble()
                    }.toFloat()
                }
            val monthlyBingeCount = dailyTotals.values.count { it >= 70f }
            
            val assessment = AlcoholCalculator.assessDrinking(
                dailyAlcohol,
                weeklyAlcohol,
                monthlyBingeCount,
                userProfile?.sex ?: Sex.UNSET
            )
            
            _drinkingAssessment.value = assessment
        }
    }

    fun deleteDrinkRecord(recordId: Long) {
        viewModelScope.launch {
            drinkRecordRepository.deleteRecord(recordId)
            loadSelectedDateRecords()
            loadMonthlyRecords()
        }
    }

    fun clearAssessment() {
        _drinkingAssessment.value = null
    }

    private fun loadMonthlyRecords() {
        viewModelScope.launch {
            val startDate = _currentMonth.value.atDay(1)
            val endDate = _currentMonth.value.atEndOfMonth()
            val userProfile = userProfileRepository.getUserProfile().first()
            
            drinkRecordRepository.getRecordsByDateRange(startDate, endDate)
                .collect { records ->
                    val dailySummaries = records.groupBy { it.date }
                        .map { (date, dayRecords) ->
                            val totalAlcohol = dayRecords.sumOf { record ->
                                AlcoholCalculator.calculateAlcoholGrams(
                                    record.type,
                                    record.volumeMl ?: 0,
                                    record.abv
                                ).toDouble()
                            }.toFloat()
                            
                            val status = when (userProfile?.sex ?: Sex.UNSET) {
                                Sex.MALE -> when {
                                    totalAlcohol <= 28f -> DrinkingStatus.APPROPRIATE
                                    totalAlcohol < 56f -> DrinkingStatus.CAUTION
                                    totalAlcohol < 70f -> DrinkingStatus.DANGER
                                    else -> DrinkingStatus.EXCESSIVE
                                }
                                Sex.FEMALE -> when {
                                    totalAlcohol <= 14f -> DrinkingStatus.APPROPRIATE
                                    totalAlcohol < 42f -> DrinkingStatus.CAUTION
                                    totalAlcohol < 70f -> DrinkingStatus.DANGER
                                    else -> DrinkingStatus.EXCESSIVE
                                }
                                Sex.UNSET -> when {
                                    totalAlcohol <= 14f -> DrinkingStatus.APPROPRIATE
                                    totalAlcohol < 42f -> DrinkingStatus.CAUTION
                                    totalAlcohol < 70f -> DrinkingStatus.DANGER
                                    else -> DrinkingStatus.EXCESSIVE
                                }
                            }
                            
                            DailySummary(
                                date = date,
                                totalMl = dayRecords.sumOf { it.volumeMl ?: 0 },
                                totalStdDrinks = totalAlcohol / 14f,
                                status = status
                            )
                        }
                    
                    _uiState.value = _uiState.value.copy(
                        monthlyRecords = records,
                        dailySummaries = dailySummaries
                    )
                }
        }
    }

    private fun loadSelectedDateRecords() {
        viewModelScope.launch {
            drinkRecordRepository.getRecordsByDate(_selectedDate.value)
                .collect { records ->
                    _uiState.value = _uiState.value.copy(selectedDateRecords = records)
                }
        }
    }
}

data class CalendarUiState(
    val monthlyRecords: List<DrinkRecord> = emptyList(),
    val selectedDateRecords: List<DrinkRecord> = emptyList(),
    val dailySummaries: List<DailySummary> = emptyList(),
    val isLoading: Boolean = false
)
