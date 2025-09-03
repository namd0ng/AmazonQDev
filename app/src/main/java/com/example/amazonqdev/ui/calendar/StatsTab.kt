package com.example.amazonqdev.ui.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.amazonqdev.ui.theme.ColorTokens
import com.example.amazonqdev.ui.theme.Spacing

@Composable
fun StatsTab() {
    var selectedPeriod by remember { mutableIntStateOf(0) }
    val periods = listOf("주간 요약", "월간 요약")
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.LG),
        verticalArrangement = Arrangement.spacedBy(Spacing.LG)
    ) {
        item {
            // 주간/월간 토글
            TabRow(
                selectedTabIndex = selectedPeriod,
                modifier = Modifier.fillMaxWidth()
            ) {
                periods.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedPeriod == index,
                        onClick = { selectedPeriod = index },
                        text = { Text(title) }
                    )
                }
            }
        }
        
        item {
            // 캐릭터 조언 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LG),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🐶",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.width(Spacing.MD))
                    Column {
                        Text(
                            text = if (selectedPeriod == 0) "이번 주 음주량이 조금 많아요" else "이번 달 전반적으로 양호해요",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "건강한 음주 습관을 유지해보세요!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        item {
            // 건강 지수 카드
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LG)
                ) {
                    Text(
                        text = "건강 지수",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.MD))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text("주의") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = ColorTokens.WarningSoft
                            )
                        )
                        Text(
                            text = "일평균 2.1잔",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(Spacing.SM))
                    
                    LinearProgressIndicator(
                        progress = { 0.6f },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        item {
            // 주별 트렌드 카드
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LG)
                ) {
                    Text(
                        text = if (selectedPeriod == 0) "주별 음주량 트렌드" else "월별 음주량 트렌드",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.MD))
                    
                    // 간단한 막대 차트 플레이스홀더
                    SimpleBarChart(
                        data = if (selectedPeriod == 0) listOf(2.1f, 1.5f, 3.2f, 0.8f, 2.7f, 1.9f, 2.3f)
                        else listOf(15.2f, 12.8f, 18.5f, 9.3f)
                    )
                }
            }
        }
        
        item {
            // 술 종류별 통계 카드
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LG)
                ) {
                    Text(
                        text = "술 종류별 통계",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.MD))
                    
                    val drinkTypes = listOf(
                        Triple("🍺", "맥주", 45),
                        Triple("🍶", "소주", 30),
                        Triple("🍷", "와인", 15),
                        Triple("🥃", "위스키", 10)
                    )
                    
                    drinkTypes.forEach { (emoji, name, percentage) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.XS),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = emoji,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(Spacing.SM))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.width(60.dp)
                            )
                            Spacer(modifier = Modifier.width(Spacing.SM))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(percentage / 100f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                            Spacer(modifier = Modifier.width(Spacing.SM))
                            Text(
                                text = "${percentage}%",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(40.dp)
                            )
                        }
                    }
                }
            }
        }
        
        item {
            // 얼굴 분석 결과 카드
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.LG)
                ) {
                    Text(
                        text = "얼굴 분석 결과",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Spacing.MD))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "분석 횟수",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "12회",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "평균 확률",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "23.5%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Float>) {
    val maxValue = data.maxOrNull() ?: 1f
    
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val barWidth = size.width / data.size * 0.7f
        val spacing = size.width / data.size * 0.3f
        
        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * size.height * 0.8f
            val x = index * (barWidth + spacing) + spacing / 2
            val y = size.height - barHeight
            
            drawRect(
                color = Color(0xFF6750A4),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
        }
    }
}