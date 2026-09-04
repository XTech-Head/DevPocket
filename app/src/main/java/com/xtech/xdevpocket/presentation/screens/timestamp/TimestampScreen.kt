package com.xtech.xdevpocket.presentation.screens.timestamp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SecondaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader

@Composable
fun TimestampScreen(
    viewModel: TimestampViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        ToolHeader(title = "Timestamp", subtitle = "Unix & date/time conversion", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        SectionHeader("Unix Timestamp \u2192 Date")
        CodeInput(
            value = uiState.timestampInput,
            onValueChange = viewModel::onTimestampInputChange,
            label = "Unix seconds or milliseconds",
            placeholder = "1754638200",
            minLines = 1,
            trailingContent = {
                SecondaryActionButton(text = "Now", onClick = viewModel::useNow)
            },
        )
        Spacer(Modifier.height(8.dp))
        PrimaryActionButton(text = "Convert", onClick = viewModel::convertToDate, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        CodeOutput(value = uiState.timestampOutput, label = "Readable Date", isError = uiState.timestampError)

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        SectionHeader("Date \u2192 Unix Timestamp")
        CodeInput(
            value = uiState.dateInput,
            onValueChange = viewModel::onDateInputChange,
            label = "Date/Time",
            placeholder = "2026-08-08 14:30:00",
            minLines = 1,
        )
        Spacer(Modifier.height(8.dp))
        PrimaryActionButton(text = "Convert", onClick = viewModel::convertToUnix, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        CodeOutput(value = uiState.dateOutput, label = "Unix Timestamp", isError = uiState.dateError)
    }
}
