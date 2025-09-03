package com.example.amazonqdev.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun CalendarScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("월별", "통계")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 제목과 추가 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "음주 기록",
                fontSize = 20.8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF030213)
            )
            
            Button(
                onClick = { /* 토스트 */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF030213),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.75.dp),
                modifier = Modifier.height(28.dp),
                contentPadding = PaddingValues(horizontal = 8.75.dp)
            ) {
                Text(
                    text = "+ 기록 추가",
                    fontSize = 12.3.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        // 하위 탭 - Figma 디자인에 맞게 커스텀 세그먼트
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .background(
                    Color(0xFFECECF0),
                    RoundedCornerShape(12.75.dp)
                )
                .padding(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(25.5.dp)
                            .background(
                                if (selectedTab == index) Color.White else Color.Transparent,
                                RoundedCornerShape(12.75.dp)
                            )
                            .clickable { selectedTab = index },
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
        
        Spacer(modifier = Modifier.height(14.dp))
        
        // 탭 내용
        when (selectedTab) {
            0 -> MonthlyTab()
            1 -> StatsTab()
        }
    }
}