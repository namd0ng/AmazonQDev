package com.example.amazonqdev.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.amazonqdev.ui.theme.Spacing

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.LG),
        verticalArrangement = Arrangement.spacedBy(Spacing.SM)
    ) {
        item {
            Text(
                text = "설정",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = Spacing.LG)
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
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Spacing.SM)
        )
        Card(
            modifier = Modifier.fillMaxWidth()
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
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Text(
                text = icon,
                style = MaterialTheme.typography.titleMedium
            )
        },
        trailingContent = {
            Text(
                text = ">",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}