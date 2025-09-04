package com.alcolook.domain

import com.alcolook.data.model.DrinkType
import com.alcolook.data.model.DrinkingLevel
import com.alcolook.data.model.DrinkingAssessment
import com.alcolook.data.model.Sex

object AlcoholCalculator {
    
    fun calculateVolumeFromUnit(
        drinkType: DrinkType,
        unit: String,
        quantity: Int
    ): Int {
        val mlPerUnit = when (unit) {
            "잔" -> when (drinkType) {
                DrinkType.SOJU -> 50
                DrinkType.BEER -> 200
                DrinkType.WINE -> 150
                DrinkType.WHISKY -> 30
                DrinkType.HIGHBALL -> 200
                DrinkType.COCKTAIL -> 150
                DrinkType.MAKGEOLLI -> 200
                DrinkType.OTHER -> 100
            }
            "병" -> when (drinkType) {
                DrinkType.SOJU -> 360
                DrinkType.BEER -> 500
                DrinkType.WINE -> 750
                DrinkType.WHISKY -> 700
                DrinkType.MAKGEOLLI -> 750
                else -> 500
            }
            "캔" -> when (drinkType) {
                DrinkType.BEER -> 355
                DrinkType.HIGHBALL -> 350
                else -> 350
            }
            "샷" -> 30
            else -> 100 // 기타
        }
        return mlPerUnit * quantity
    }
    
    fun calculateAlcoholGrams(
        drinkType: DrinkType,
        volumeMl: Int,
        abv: Float? = null
    ): Float {
        val alcoholByVolume = abv ?: getDefaultAbv(drinkType)
        return volumeMl * (alcoholByVolume / 100f) * 0.789f
    }
    
    private fun getDefaultAbv(drinkType: DrinkType): Float = when (drinkType) {
        DrinkType.SOJU -> 16f
        DrinkType.BEER -> 4.5f
        DrinkType.WINE -> 15f
        DrinkType.WHISKY -> 40f
        DrinkType.HIGHBALL -> 7f
        DrinkType.COCKTAIL -> 15f
        DrinkType.MAKGEOLLI -> 6f
        DrinkType.OTHER -> 12f
    }
    
    fun assessDrinking(
        dailyAlcoholGrams: Float,
        weeklyAlcoholGrams: Float,
        monthlyBingeCount: Int,
        sex: Sex
    ): DrinkingAssessment {
        val level = when {
            isExcessive(dailyAlcoholGrams, monthlyBingeCount, sex) -> DrinkingLevel.EXCESSIVE
            isBinge(dailyAlcoholGrams, sex) -> DrinkingLevel.BINGE
            isCaution(dailyAlcoholGrams, weeklyAlcoholGrams, sex) -> DrinkingLevel.LOW_RISK
            else -> DrinkingLevel.APPROPRIATE
        }
        
        return DrinkingAssessment(
            level = level,
            alcoholGrams = dailyAlcoholGrams,
            message = getFeedbackMessage(level)
        )
    }
    
    private fun isExcessive(dailyGrams: Float, monthlyBingeCount: Int, sex: Sex): Boolean {
        // 월 5회 이상 폭음 또는 일일 알코올량이 매우 높은 경우
        return monthlyBingeCount >= 5 || when (sex) {
            Sex.MALE -> dailyGrams >= 70f  // 남성 70g 이상
            Sex.FEMALE -> dailyGrams >= 70f // 여성 70g 이상  
            Sex.UNSET -> dailyGrams >= 70f  // 여성과 동일
        }
    }
    
    private fun isBinge(dailyGrams: Float, sex: Sex): Boolean = when (sex) {
        Sex.MALE -> dailyGrams >= 56f && dailyGrams < 70f    // 56g~70g 미만
        Sex.FEMALE -> dailyGrams >= 42f && dailyGrams < 70f  // 42g~70g 미만
        Sex.UNSET -> dailyGrams >= 42f && dailyGrams < 70f   // 여성과 동일
    }
    
    private fun isCaution(dailyGrams: Float, weeklyGrams: Float, sex: Sex): Boolean {
        return when (sex) {
            Sex.MALE -> dailyGrams >= 28f && dailyGrams < 56f || weeklyGrams > 196f
            Sex.FEMALE -> dailyGrams >= 14f && dailyGrams < 42f || weeklyGrams > 98f
            Sex.UNSET -> dailyGrams >= 14f && dailyGrams < 42f || weeklyGrams > 98f // 여성과 동일
        }
    }
    
    private fun getFeedbackMessage(level: DrinkingLevel): String {
        val messages = when (level) {
            DrinkingLevel.APPROPRIATE -> listOf(
                "오늘은 딱 알맞게 즐기셨네요 🍷 균형 잡힌 음주, 멋져요!",
                "좋습니다 👍 내일도 상쾌하게 일어날 수 있겠네요.",
                "이 정도면 건강에 큰 무리 없어요. 현명한 선택이네요!",
                "오늘은 깔끔하게 딱 적정량만! 자기 관리 잘하시네요 👏"
            )
            DrinkingLevel.LOW_RISK -> listOf(
                "조금은 과했네요 🤔 내일은 물 많이 드시고 쉬어주세요.",
                "이 정도면 괜찮지만, 매일 반복되면 몸이 힘들 수 있어요.",
                "슬슬 간이 피곤해질지도… 내일은 가볍게 보내는 게 어떨까요?",
                "컨디션 체크하면서 마시는 것도 중요해요 😉"
            )
            DrinkingLevel.BINGE -> listOf(
                "이건 위험한 수준이에요 🚨 속도를 줄이셔야 합니다.",
                "오늘은 좀 과격했네요… 간이 놀랐을 거예요 😵",
                "이러다 내일 숙취와 함께 고통받을 수도 있어요 🤢",
                "가끔은 괜찮지만, 자주 반복되면 건강에 큰 부담이 돼요."
            )
            DrinkingLevel.EXCESSIVE -> listOf(
                "심각한 음주 패턴이 보입니다 ⚠️ 전문가 상담을 고려하세요.",
                "몸이 보내는 신호를 무시하지 마세요. 위험해요.",
                "이 정도면 간이 SOS를 보내고 있을 거예요 🆘",
                "한 번쯤 음주 습관을 점검해보는 게 어떨까요?",
                "과음은 삶의 질을 떨어뜨립니다. 지금이 바꿀 때예요."
            )
        }
        return messages.random()
    }
}
