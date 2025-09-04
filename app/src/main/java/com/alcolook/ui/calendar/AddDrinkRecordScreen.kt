package com.alcolook.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alcolook.data.model.DrinkType

@Composable
fun AddDrinkRecordScreen(
    onSave: (DrinkType, String, Int, Float?, String?) -> Unit,
    onCancel: () -> Unit
) {
    var selectedDrinkType by remember { mutableStateOf(DrinkType.SOJU) }
    var selectedUnit by remember { mutableStateOf("잔") }
    var quantityText by remember { mutableStateOf("") }
    var abvText by remember { mutableStateOf("") }
    var memo by remember { mutableStateOf("") }
    
    val units = listOf("잔", "병", "캔", "샷", "기타")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 제목
        Text(
            text = "기록 추가",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF030213)
        )
        
        // 술 종류 선택
        Text(
            text = "술 종류",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF030213)
        )
        
        // 2줄로 배치
        val drinkTypes = DrinkType.values().toList()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // 첫 번째 줄 (4개)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                drinkTypes.take(4).forEach { drinkType ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedDrinkType == drinkType,
                                onClick = { selectedDrinkType = drinkType }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDrinkType == drinkType,
                            onClick = { selectedDrinkType = drinkType },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF030213)
                            ),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = getDrinkTypeName(drinkType),
                            fontSize = 12.sp,
                            color = Color(0xFF030213)
                        )
                    }
                }
            }
            
            // 두 번째 줄 (나머지)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                drinkTypes.drop(4).forEach { drinkType ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selectedDrinkType == drinkType,
                                onClick = { selectedDrinkType = drinkType }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDrinkType == drinkType,
                            onClick = { selectedDrinkType = drinkType },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF030213)
                            ),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = getDrinkTypeName(drinkType),
                            fontSize = 12.sp,
                            color = Color(0xFF030213)
                        )
                    }
                }
                // 빈 공간 채우기 (4개 미만일 경우)
                repeat(4 - drinkTypes.drop(4).size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        
        // 단위 선택
        Text(
            text = "단위",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF030213)
        )
        
        // 단위 선택 - 앱 스타일에 맞는 세그먼트 컨트롤
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFECECF0),
                    RoundedCornerShape(12.dp)
                )
                .padding(3.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                units.forEach { unit ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(
                                if (selectedUnit == unit) Color.White else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedUnit = unit },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = unit,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF030213)
                        )
                    }
                }
            }
        }
        
        // 수량 입력
        OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { 
                Text(
                    "수량", 
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                ) 
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF030213),
                focusedLabelColor = Color(0xFF030213)
            ),
            shape = RoundedCornerShape(8.dp)
        )
        
        // 도수 입력
        OutlinedTextField(
            value = abvText,
            onValueChange = { abvText = it },
            label = { 
                Text(
                    "도수 (%) - 선택사항", 
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                ) 
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF030213),
                focusedLabelColor = Color(0xFF030213)
            ),
            shape = RoundedCornerShape(8.dp)
        )
        
        // 메모 입력
        OutlinedTextField(
            value = memo,
            onValueChange = { memo = it },
            label = { 
                Text(
                    "메모 (선택사항)", 
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF030213),
                focusedLabelColor = Color(0xFF030213)
            ),
            shape = RoundedCornerShape(8.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 버튼들 - 앱 스타일에 맞게
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 취소 버튼
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF030213)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF030213))
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "취소",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 저장 버튼
            Button(
                onClick = {
                    val quantity = quantityText.toIntOrNull() ?: 0
                    val abv = abvText.toFloatOrNull()
                    val memoText = memo.takeIf { it.isNotBlank() }
                    if (quantity > 0) {
                        onSave(selectedDrinkType, selectedUnit, quantity, abv, memoText)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                enabled = quantityText.toIntOrNull()?.let { it > 0 } == true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF030213),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFCCCCCC)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "저장",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun getDrinkTypeName(drinkType: DrinkType): String = when (drinkType) {
    DrinkType.SOJU -> "소주"
    DrinkType.BEER -> "맥주"
    DrinkType.WINE -> "와인"
    DrinkType.WHISKY -> "위스키"
    DrinkType.HIGHBALL -> "하이볼"
    DrinkType.COCKTAIL -> "칵테일"
    DrinkType.MAKGEOLLI -> "막걸리"
    DrinkType.OTHER -> "기타"
}
