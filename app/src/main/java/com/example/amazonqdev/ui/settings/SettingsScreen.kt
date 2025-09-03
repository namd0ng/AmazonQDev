package com.example.amazonqdev.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "설정",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF030213),
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }
        
        item {
            SettingsSection(title = "개인 정보") {
                SettingsItem(
                    icon = "👤",
                    title = "프로필 설정",
                    subtitle = "성별, 연령대 설정",
                    onClick = { }
                )
                Divider(color = Color(0xFFE9ECF1))
                SettingsItem(
                    icon = "🏁",
                    title = "주간 목표",
                    subtitle = "권장 음주량 설정",
                    onClick = { }
                )
            }
        }
        
        item {
            SettingsSection(title = "데이터 관리") {
                SettingsItem(
                    icon = "💾",
                    title = "데이터 백업",
                    subtitle = "로컬 데이터 내보내기",
                    onClick = { }
                )
                Divider(color = Color(0xFFE9ECF1))
                SettingsItem(
                    icon = "🗑️",
                    title = "데이터 전체 삭제",
                    subtitle = "모든 기록 삭제",
                    onClick = { }
                )
            }
        }
        
        item {
            SettingsSection(title = "앱 정보") {
                SettingsItem(
                    icon = "ℹ️",
                    title = "앱 정보",
                    subtitle = "버전 1.0.0",
                    onClick = { }
                )
                Divider(color = Color(0xFFE9ECF1))
                SettingsItem(
                    icon = "❓",
                    title = "도움말",
                    subtitle = "사용법 및 면책 사항",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF3B82F6),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { 
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF030213)
            )
        },
        supportingContent = { 
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = Color(0xFF717182)
            )
        },
        leadingContent = {
            Text(
                text = icon,
                fontSize = 20.sp
            )
        },
        trailingContent = {
            Text(
                text = ">",
                fontSize = 16.sp,
                color = Color(0xFF717182)
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}