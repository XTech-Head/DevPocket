package com.xtech.xdevpocket.presentation.screens.caseconverter

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.utilities.CaseConverterUtility
import com.xtech.xdevpocket.presentation.components.ClearButton
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SecondaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import kotlinx.coroutines.launch

@Composable
fun CaseConverterScreen(
    viewModel: CaseConverterViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        ToolHeader(title = "Case Converter", subtitle = "camelCase, snake_case, and more", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.input,
            onValueChange = viewModel::onInputChange,
            placeholder = "hello world example",
            minLines = 3,
            trailingContent = {
                SecondaryActionButton(
                    text = "Paste",
                    onClick = { ClipboardHelper.pasteFromClipboard(context)?.let(viewModel::onInputChange) },
                )
            },
        )

        Spacer(Modifier.height(16.dp))
        SectionHeader("Target Case")
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            CaseConverterUtility.CaseType.entries.forEach { type ->
                FilterChip(
                    selected = uiState.selectedCase == type,
                    onClick = { viewModel.onCaseTypeChange(type) },
                    label = { Text(type.label) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Aqua.copy(alpha = 0.18f),
                        selectedLabelColor = Aqua,
                    ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Convert", onClick = viewModel::convert, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        CodeOutput(
            value = uiState.output,
            isError = uiState.isError,
            trailingContent = {
                Row {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "Converted text", uiState.output)
                        scope.launch { snackbarHostState.showSnackbar("Copied") }
                    })
                    com.xtech.xdevpocket.presentation.components.ShareButton(onClick = {
                        com.xtech.xdevpocket.presentation.components.ShareHelper.share(context, uiState.output)
                    })
                    ClearButton(onClick = viewModel::clear)
                }
            },
        )
    }
}
