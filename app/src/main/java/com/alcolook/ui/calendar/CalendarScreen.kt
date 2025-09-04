package com.alcolook.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showAddDialog by viewModel.showAddDrinkDialog.collectAsState()
    val assessment by viewModel.drinkingAssessment.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        CalendarContent(
            uiState = uiState,
            onDateSelected = viewModel::selectDate,
            onMonthChanged = viewModel::changeMonth
        )

        // FAB for adding drink record
        FloatingActionButton(
            onClick = { viewModel.showAddDrinkDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "기록 추가")
        }
    }

    // Add drink dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddDrinkDialog() },
            confirmButton = {},
            text = {
                AddDrinkRecordScreen(
                    onSave = { drinkType, unit, quantity, abv, memo ->
                        viewModel.addDrinkRecord(drinkType, unit, quantity, abv, memo)
                    },
                    onCancel = { viewModel.hideAddDrinkDialog() }
                )
            }
        )
    }

    // Assessment result dialog
    assessment?.let { result ->
        AlertDialog(
            onDismissRequest = { viewModel.clearAssessment() },
            confirmButton = {},
            text = {
                DrinkingAssessmentScreen(
                    assessment = result,
                    onConfirm = { viewModel.clearAssessment() }
                )
            }
        )
    }
}

@Composable
private fun CalendarContent(
    uiState: CalendarUiState,
    onDateSelected: (java.time.LocalDate) -> Unit,
    onMonthChanged: (java.time.YearMonth) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Calendar grid and other existing content
        Text("캘린더 내용 (기존 구현 유지)")
    }
}
