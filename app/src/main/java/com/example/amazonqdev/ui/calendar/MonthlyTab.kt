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
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyTab() {
    var showBottomSheet by remember { mutableStateOf(false) }
    
    // 실제 데이터 연결
    val drinkViewModel: com.alcolook.ui.calendar.CalendarViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val uiState by drinkViewModel.uiState.collectAsState()
    val currentMonth by drinkViewModel.currentMonth.collectAsState()
    val selectedDate by drinkViewModel.selectedDate.collectAsState()
    
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
                                color = Color(0xFF030213),
                                modifier = Modifier
                                    .clickable { 
                                        drinkViewModel.changeMonth(currentMonth.minusMonths(1))
                                    }
                                    .padding(horizontal = 8.dp, vertical = 12.dp)
                            )
                            Text(
                                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF030213)
                            )
                            Text(
                                text = "▶",
                                fontSize = 14.sp,
                                color = Color(0xFF030213),
                                modifier = Modifier
                                    .clickable { 
                                        drinkViewModel.changeMonth(currentMonth.plusMonths(1))
                                    }
                                    .padding(horizontal = 8.dp, vertical = 12.dp)
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
                        val firstDayOfMonth = currentMonth.atDay(1)
                        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일=0, 월요일=1, ...
                        val daysInMonth = currentMonth.lengthOfMonth()
                        
                        repeat(6) { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(7) { col ->
                                    val dayNumber = row * 7 + col + 1 - firstDayOfWeek
                                    
                                    if (dayNumber > 0 && dayNumber <= daysInMonth) {
                                        val date = currentMonth.atDay(dayNumber)
                                        val dailySummary = uiState.dailySummaries.find { it.date == date }
                                        
                                        val status = when {
                                            date == selectedDate -> "selected"
                                            dailySummary != null -> when (dailySummary.status) {
                                                com.alcolook.data.model.DrinkingStatus.APPROPRIATE -> "APPROPRIATE"
                                                com.alcolook.data.model.DrinkingStatus.CAUTION -> "LOW_RISK"
                                                com.alcolook.data.model.DrinkingStatus.DANGER -> "BINGE"
                                                com.alcolook.data.model.DrinkingStatus.EXCESSIVE -> "EXCESSIVE"
                                            }
                                            else -> "normal"
                                        }
                                        
                                        DayCell(
                                            day = dayNumber,
                                            status = status,
                                            onClick = { 
                                                drinkViewModel.selectDate(date)
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        // 빈 공간
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
                            text = "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일 기록",
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
                    
                    // 선택된 날짜의 기록 목록
                    if (selectedDate != null) {
                        val selectedDateRecords = uiState.monthlyRecords.filter { it.date == selectedDate }
                        
                        Text(
                            text = "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일 기록",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF030213),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        if (selectedDateRecords.isNotEmpty()) {
                            selectedDateRecords.forEach { record ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "${getDrinkTypeName(record.type)} ${record.quantity}${record.unit}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color(0xFF030213)
                                                )
                                                Text(
                                                    text = "${record.volumeMl}ml",
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF717182)
                                                )
                                            }
                                            record.note?.let { note ->
                                                Text(
                                                    text = "메모: $note",
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF717182),
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        }
                                        
                                        // 삭제 버튼
                                        OutlinedButton(
                                            onClick = { 
                                                drinkViewModel.deleteDrinkRecord(record.id)
                                            },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color(0xFFDC3545)
                                            ),
                                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFDC3545))
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier
                                                .height(32.dp)
                                                .padding(start = 8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp)
                                        ) {
                                            Text(
                                                text = "삭제",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
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
                        }
                    } else {
                        // 날짜가 선택되지 않은 경우
                        Text(
                            text = "🍺",
                            fontSize = 42.sp,
                            modifier = Modifier.alpha(0.3f)
                        )
                        
                        Spacer(modifier = Modifier.height(14.dp))
                        
                        Text(
                            text = "날짜를 선택해주세요",
                            fontSize = 14.sp,
                            color = Color(0xFF717182),
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(14.dp))
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
            val selectedDateSummary = uiState.dailySummaries.find { it.date == selectedDate }
            val selectedDateRecords = uiState.selectedDateRecords
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(21.dp)
            ) {
                Text(
                    text = "${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일 요약",
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
                            text = "${selectedDateSummary?.totalMl ?: 0}ml",
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
                            text = "${String.format("%.1f", selectedDateSummary?.totalStdDrinks ?: 0f)}잔",
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
                            text = when (selectedDateSummary?.status) {
                                com.alcolook.data.model.DrinkingStatus.APPROPRIATE -> "적정"
                                com.alcolook.data.model.DrinkingStatus.CAUTION -> "주의"
                                com.alcolook.data.model.DrinkingStatus.DANGER -> "과음"
                                com.alcolook.data.model.DrinkingStatus.EXCESSIVE -> "위험"
                                else -> "기록 없음"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF030213)
                        )
                    }
                }
                
                // 기록 목록 표시
                if (selectedDateRecords.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(21.dp))
                    Text(
                        text = "상세 기록",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF030213)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    selectedDateRecords.forEach { record ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${getDrinkTypeName(record.type)} ${record.quantity}${record.unit}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF030213)
                                        )
                                        Text(
                                            text = "${record.volumeMl}ml",
                                            fontSize = 14.sp,
                                            color = Color(0xFF717182)
                                        )
                                    }
                                    record.note?.let { note ->
                                        Text(
                                            text = "메모: $note",
                                            fontSize = 12.sp,
                                            color = Color(0xFF717182),
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                                
                                // 삭제 버튼
                                OutlinedButton(
                                    onClick = { 
                                        drinkViewModel.deleteDrinkRecord(record.id)
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFDC3545)
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFDC3545))
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .height(32.dp)
                                        .padding(start = 8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                ) {
                                    Text(
                                        text = "삭제",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    }
                }
                
                Spacer(modifier = Modifier.height(21.dp))
            }
        }
    }

private fun getDrinkTypeName(drinkType: com.alcolook.data.model.DrinkType): String = when (drinkType) {
    com.alcolook.data.model.DrinkType.SOJU -> "소주"
    com.alcolook.data.model.DrinkType.BEER -> "맥주"
    com.alcolook.data.model.DrinkType.WINE -> "와인"
    com.alcolook.data.model.DrinkType.WHISKY -> "위스키"
    com.alcolook.data.model.DrinkType.HIGHBALL -> "하이볼"
    com.alcolook.data.model.DrinkType.COCKTAIL -> "칵테일"
    com.alcolook.data.model.DrinkType.MAKGEOLLI -> "막걸리"
    com.alcolook.data.model.DrinkType.OTHER -> "기타"
}

@Composable
fun DayCell(
    day: Int,
    status: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        "APPROPRIATE" -> Color(0xFFE8F5E8)    // 적정 - 초록색
        "LOW_RISK" -> Color(0xFFFFF4E5)       // 저위험 - 노란색
        "BINGE" -> Color(0xFFFFEBEE)          // 폭음 - 빨간색
        "EXCESSIVE" -> Color(0xFF2C2C2C)      // 과량 - 검정색
        "selected" -> Color(0xFF030213)       // 선택됨
        else -> Color(0xFFECECF0)             // 기록 없음 - 회색
    }
    
    val textColor = when (status) {
        "selected", "EXCESSIVE" -> Color.White
        "APPROPRIATE" -> Color(0xFF2E7D32)
        "LOW_RISK" -> Color(0xFFE65100)
        "BINGE" -> Color(0xFFC62828)
        else -> Color(0xFF030213)
    }
    
    val dotColor = when (status) {
        "selected", "EXCESSIVE" -> Color.White
        "APPROPRIATE" -> Color(0xFF2E7D32)
        "LOW_RISK" -> Color(0xFFE65100)
        "BINGE" -> Color(0xFFC62828)
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
            
            // 기록이 있는 날에만 점 표시
            if (status in listOf("APPROPRIATE", "LOW_RISK", "BINGE", "EXCESSIVE", "selected")) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(dotColor, CircleShape)
                )
            }
        }
    }
}