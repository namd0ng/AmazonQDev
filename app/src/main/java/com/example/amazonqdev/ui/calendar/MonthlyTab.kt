package com.example.amazonqdev.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyTab() {
    var showBottomSheet by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // 달력 카드 - Figma 스펙: width=738dp, height≈556dp
            Card(
                modifier = Modifier
                    .width(738.dp)
                    .height(406.dp),
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color(0x1A000000))
            ) {
                Column(
                    modifier = Modifier.padding(21.dp)
                ) {
                    // 월 네비게이션 - 상단 57dp 높이 영역
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "◀",
                                fontSize = 14.sp,
                                color = Color(0xFF030213)
                            )
                            Text(
                                text = "2025년 9월",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF030213)
                            )
                            Text(
                                text = "▶",
                                fontSize = 14.sp,
                                color = Color(0xFF030213)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(21.dp))
                    
                    // 요일 헤더 - 32dp 높이
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day,
                                    fontSize = 12.sp,
                                    color = Color(0xFF717182)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(7.dp))
                    
                    // 날짜 그리드 - 7열 × 6행, 균등 크기
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(6) { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(7) { col ->
                                    val day = row * 7 + col + 1
                                    if (day <= 30) {
                                        DayCell(
                                            day = day,
                                            status = when {
                                                day == 2 -> "warning"
                                                day == 3 -> "selected"
                                                else -> "normal"
                                            },
                                            onClick = { showBottomSheet = true },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        item {
            // 선택일 기록 카드
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "9월 3일 기록",
                            fontSize = 14.sp,
                            color = Color(0xFF030213)
                        )
                        
                        Surface(
                            onClick = { showBottomSheet = true },
                            shape = RoundedCornerShape(7.dp),
                            modifier = Modifier.height(28.dp),
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9ECF1))
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📊 요약 보기",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF030213)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    // 빈 상태
                    Text(
                        text = "🍺",
                        fontSize = 42.sp,
                        modifier = Modifier.alpha(0.3f)
                    )
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Text(
                        text = "기록된 음주가 없습니다",
                        fontSize = 14.sp,
                        color = Color(0xFF717182),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Surface(
                        onClick = { },
                        shape = RoundedCornerShape(7.dp),
                        modifier = Modifier.height(28.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9ECF1))
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ 첫 기록 추가하기",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF030213)
                            )
                        }
                    }
                }
            }
        }
    }
    
    // 요약 바텀시트
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(21.dp)
            ) {
                Text(
                    text = "9월 3일 요약",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF030213)
                )
                
                Spacer(modifier = Modifier.height(21.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "총 음주량",
                            fontSize = 12.sp,
                            color = Color(0xFF717182)
                        )
                        Text(
                            text = "0ml",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF030213)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "표준잔수",
                            fontSize = 12.sp,
                            color = Color(0xFF717182)
                        )
                        Text(
                            text = "0잔",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF030213)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "상태",
                            fontSize = 12.sp,
                            color = Color(0xFF717182)
                        )
                        Text(
                            text = "양호",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF030213)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(42.dp))
            }
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    status: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        "warning" -> Color(0xFFFEF9C2)
        "selected" -> Color(0xFF030213)
        else -> Color(0xFFECECF0)
    }
    
    val textColor = when (status) {
        "selected" -> Color.White
        else -> Color(0xFF030213)
    }
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor, RoundedCornerShape(7.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.toString(),
                fontSize = 12.sp,
                color = textColor
            )
            
            if (status == "warning" || status == "selected") {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            if (status == "selected") Color.White else Color(0xFF030213),
                            CircleShape
                        )
                )
            }
        }
    }
}