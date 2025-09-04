package com.alcolook.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alcolook.data.model.DrinkingAssessment
import com.alcolook.data.model.DrinkingLevel

@Composable
fun DrinkingAssessmentScreen(
    assessment: DrinkingAssessment,
    onConfirm: () -> Unit
) {
    val (backgroundColor, textColor) = getAssessmentColors(assessment.level)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = getLevelTitle(assessment.level),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                
                Text(
                    text = "순수 알코올: ${String.format("%.1f", assessment.alcoholGrams)}g",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
                
                Text(
                    text = assessment.message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = textColor
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("확인")
        }
    }
}

private fun getAssessmentColors(level: DrinkingLevel): Pair<Color, Color> = when (level) {
    DrinkingLevel.APPROPRIATE -> Color(0xFFE8F5E8) to Color(0xFF2E7D32)
    DrinkingLevel.LOW_RISK -> Color(0xFFFFF4E5) to Color(0xFFE65100)
    DrinkingLevel.BINGE -> Color(0xFFFFEBEE) to Color(0xFFC62828)
    DrinkingLevel.EXCESSIVE -> Color(0xFF2C2C2C) to Color(0xFFFFFFFF)
}

private fun getLevelTitle(level: DrinkingLevel): String = when (level) {
    DrinkingLevel.APPROPRIATE -> "적정"
    DrinkingLevel.LOW_RISK -> "주의"
    DrinkingLevel.BINGE -> "과음"
    DrinkingLevel.EXCESSIVE -> "위험"
}
