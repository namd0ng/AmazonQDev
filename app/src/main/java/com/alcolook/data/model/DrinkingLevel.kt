package com.alcolook.data.model

enum class DrinkingLevel {
    APPROPRIATE,    // 적정
    LOW_RISK,      // 저위험
    BINGE,         // 폭음
    EXCESSIVE      // 과량
}

enum class DrinkType {
    SOJU, BEER, WINE, WHISKY, HIGHBALL, COCKTAIL, MAKGEOLLI, OTHER
}

enum class Sex {
    MALE, FEMALE, UNSET
}

enum class DrinkingStatus {
    APPROPRIATE, CAUTION, DANGER, EXCESSIVE
}

data class DrinkingAssessment(
    val level: DrinkingLevel,
    val alcoholGrams: Float,
    val message: String
)
