package com.alcolook.data.model

import java.time.LocalDate

data class DrinkRecord(
    val id: Long = 0,
    val date: LocalDate,
    val type: DrinkType,
    val unit: String,
    val quantity: Int,
    val abv: Float?,
    val volumeMl: Int?,
    val note: String?,
    val analysisProb: Float?
)

data class DailySummary(
    val date: LocalDate,
    val totalMl: Int,
    val totalStdDrinks: Float,
    val status: DrinkingStatus
)

data class UserProfile(
    val sex: Sex = Sex.UNSET,
    val isSenior65: Boolean = false,
    val weeklyGoalStdDrinks: Int? = null
)
