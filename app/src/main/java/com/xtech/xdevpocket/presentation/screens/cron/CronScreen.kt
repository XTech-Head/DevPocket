package com.xtech.xdevpocket.presentation.screens.cron

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader

@Composable
fun CronScreen(
    viewModel: CronViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        ToolHeader(
            title = "Cron Helper",
            subtitle = "Plain-English cron descriptions",
            onBack = onBack,
            helpText = "A cron expression has 5 fields, in order: minute, hour, day-of-month, month, " +
                "day-of-week. Use * to mean \"any value\" and */N to mean \"every N units\". For example, " +
                "*/5 * * * * means every 5 minutes. Tap an example chip to try one.",
        )
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.expression,
            onValueChange = viewModel::onExpressionChange,
            label = "Cron Expression",
            placeholder = "*/5 * * * *",
            minLines = 1,
        )

        Spacer(Modifier.height(12.dp))
        SectionHeader("Examples")
        Row(modifier = Modifier.fillMaxWidth()) {
            CronViewModel.examples.forEach { (expr, label) ->
                AssistChip(
                    onClick = { viewModel.applyExample(expr) },
                    label = { Text(label) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Describe", onClick = viewModel::describe, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))
        CodeOutput(value = uiState.description, label = "Description", isError = uiState.isError)
    }
}
