package com.example.amazonqdev.ui.settings

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amazonqdev.data.DataExportManager
import com.example.amazonqdev.data.DataResetManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var selectedGender by remember { mutableStateOf("") }
    var selectedAge by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAppInfoDialog by remember { mutableStateOf(false) }
    var showHelpSheet by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val dataExportManager = remember { DataExportManager() }
    val dataResetManager = remember { DataResetManager(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val content = dataExportManager.generateBackupData(selectedGender, selectedAge, selectedGoal, selectedTheme)
                val success = dataExportManager.writeToUri(context, uri, content)
                
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        if (success) "백업이 완료되었습니다" else "백업에 실패했습니다"
                    )
                }
            }
        }
    }
    
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
                fontSize = 20.8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C20),
                modifier = Modifier.padding(bottom = 14.dp)
            )
        }
        
        item {
            SettingsSection(title = "개인 정보") {
                DropdownSettingsItem(
                    icon = "👤",
                    title = "성별",
                    options = listOf("남성", "여성"),
                    selectedValue = selectedGender,
                    onValueChange = { selectedGender = it }
                )
                DropdownSettingsItem(
                    icon = "🎂",
                    title = "연령대",
                    options = listOf("20대", "30대", "40대", "50대", "60~64세", "65세 이상"),
                    selectedValue = selectedAge,
                    onValueChange = { selectedAge = it }
                )
                DropdownSettingsItem(
                    icon = "🏁",
                    title = "주간 목표",
                    options = listOf("7잔(권장)", "14잔(저위험)", "21잔(최대)"),
                    selectedValue = selectedGoal,
                    onValueChange = { selectedGoal = it }
                )
                DropdownSettingsItem(
                    icon = "🎨",
                    title = "테마 설정",
                    options = listOf("시스템", "다크", "라이트"),
                    selectedValue = selectedTheme,
                    onValueChange = { selectedTheme = it }
                )
            }
        }
        
        item {
            SettingsSection(title = "데이터 관리") {
                SettingsItem(
                    icon = "💾",
                    title = "데이터 백업",
                    subtitle = "로컬 데이터 내보내기",
                    onClick = {
                        exportLauncher.launch(dataExportManager.createExportIntent())
                    }
                )
                SettingsItem(
                    icon = "🗑️",
                    title = "데이터 전체 삭제",
                    subtitle = "모든 기록 삭제",
                    onClick = {
                        showDeleteDialog = true
                    }
                )
            }
        }
        
        item {
            SettingsSection(title = "앱 정보") {
                SettingsItem(
                    icon = "ℹ️",
                    title = "앱 정보",
                    subtitle = "버전 1.0.0",
                    onClick = { showAppInfoDialog = true }
                )
                SettingsItem(
                    icon = "❓",
                    title = "도움말",
                    subtitle = "사용법 및 면책 사항",
                    onClick = { showHelpSheet = true }
                )
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "데이터 전체 삭제",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "모든 기록과 설정이 영구적으로 삭제됩니다. 이 작업은 되돌릴 수 없습니다.",
                    color = Color(0xFF717182)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        val success = dataResetManager.resetAllData()
                        
                        selectedGender = ""
                        selectedAge = ""
                        selectedGoal = ""
                        selectedTheme = ""
                        
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                if (success) "모든 데이터가 삭제되었습니다" else "삭제에 실패했습니다"
                            )
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("취소")
                }
            }
        )
    }
    
    if (showAppInfoDialog) {
        AlertDialog(
            onDismissRequest = { showAppInfoDialog = false },
            title = {
                Text(
                    text = "AlcoLook",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column {
                    Text(
                        text = "버전: 1.0.0",
                        fontSize = 14.sp,
                        color = Color(0xFF717182),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "모든 데이터는 기기 내에 저장됩니다. 클라우드 업로드/계정 동기화 기능이 없습니다.",
                        fontSize = 14.sp,
                        color = Color(0xFF030213),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "오픈소스 라이선스를 준수하여 제작되었습니다.",
                        fontSize = 12.sp,
                        color = Color(0xFF717182)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showAppInfoDialog = false }
                ) {
                    Text("확인")
                }
            }
        )
    }
    
    if (showHelpSheet) {
        AlertDialog(
            onDismissRequest = { showHelpSheet = false },
            title = {
                Text(
                    text = "도움말 및 면책 사항",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "사용 안내",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "분석 결과는 참고 지표입니다.",
                        fontSize = 12.sp,
                        color = Color(0xFF030213),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Text(
                        text = "법적 고지 및 면책",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    
                    val disclaimerText = """
• 이 앱은 의료기기/진단 도구가 아닙니다. 질병의 진단·치료·예방 목적에 사용할 수 없습니다.
• 결과는 혈중알코올농도(BAC) 측정기를 대체하지 않습니다.
• 운전 가능 여부 판단에 절대 사용하지 마세요.
• 결과는 조명·각도·표정 등 환경에 따라 부정확할 수 있습니다. 오판 책임은 사용자에게 있습니다.
• 이 앱은 온디바이스로 동작하며, 기본적으로 서버 전송을 하지 않습니다. 설정에서 데이터 전체 삭제가 가능합니다.
• 응급 상황(알코올 중독 의심, 의식 저하 등)에서는 즉시 지역 응급번호로 연락하거나 의료기관을 이용하세요.

데이터 관리: 설정 > 데이터 관리
                    """.trimIndent()
                    
                    Text(
                        text = disclaimerText,
                        fontSize = 12.sp,
                        color = Color(0xFF030213),
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showHelpSheet = false }
                ) {
                    Text("확인")
                }
            }
        )
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE9ECF1))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
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
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C20)
            )
        },
        supportingContent = { 
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        },
        leadingContent = {
            Text(
                text = icon,
                fontSize = 18.sp
            )
        },
        trailingContent = {
            Text(
                text = "▶",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
}

@Composable
fun DropdownSettingsItem(
    icon: String,
    title: String,
    options: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ListItem(
        headlineContent = { 
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C20)
            )
        },
        leadingContent = {
            Text(
                text = icon,
                fontSize = 18.sp
            )
        },
        trailingContent = {
            Box {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFECECF0),
                            shape = RoundedCornerShape(12.75.dp)
                        )
                        .clickable { expanded = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .defaultMinSize(minWidth = 60.dp, minHeight = 25.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = selectedValue.ifEmpty { "선택" },
                            fontSize = 12.3.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedValue.isEmpty()) Color(0xFF6B7280) else Color(0xFF1A1C20)
                        )
                        Text(
                            text = "▼",
                            fontSize = 8.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    fontSize = 12.3.sp,
                                    color = Color(0xFF1A1C20)
                                )
                            },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            },
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}