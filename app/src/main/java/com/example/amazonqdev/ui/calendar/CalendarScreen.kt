package com.example.amazonqdev.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.amazonqdev.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("월별", "통계")
    
    Column(modifier = Modifier.fillMaxSize()) {
        // 상단 제목과 추가 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.LG),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "음주 기록",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            TextButton(
                onClick = { /* 토스트 */ }
            ) {
                Text(
                    text = "+ 기록 추가",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // 하위 탭
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        // 탭 내용
        when (selectedTab) {
            0 -> MonthlyTab()
            1 -> StatsTab()
        }
    }
}