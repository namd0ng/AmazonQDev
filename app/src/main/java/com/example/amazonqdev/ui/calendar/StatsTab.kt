package com.example.amazonqdev.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsTab() {
    var selectedPeriod by remember { mutableIntStateOf(0) }
    val periods = listOf("주간 요약", "월간 요약")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // 주간/월간 토글
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFECECF0),
                        RoundedCornerShape(12.75.dp)
                    )
                    .padding(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    periods.forEachIndexed { index, title ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(25.5.dp)
                                .background(
                                    if (selectedPeriod == index) Color.White else Color.Transparent,
                                    RoundedCornerShape(12.75.dp)
                                )
                                .clickable { selectedPeriod = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.3.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF030213)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // 플레이스홀더 카드들
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.75.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(21.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "통계 데이터 준비 중...",
                        fontSize = 16.sp,
                        color = Color(0xFF717182)
                    )
                }
            }
        }
    }
}