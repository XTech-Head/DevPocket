package com.xtech.xdevpocket.presentation.screens.hash

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.utilities.HashAlgorithm
import com.xtech.xdevpocket.presentation.components.ClearButton
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import com.xtech.xdevpocket.presentation.theme.Danger
import kotlinx.coroutines.launch

@Composable
fun HashScreen(
    viewModel: HashViewModel,
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
        ToolHeader(
            title = "Hash Generator",
            subtitle = "MD5, SHA-1, SHA-256+",
            onBack = onBack,
            helpText = "A hash turns any input into a fixed-length fingerprint — the same input " +
                "always produces the same hash, but you can't reverse a hash back into the original " +
                "text. MD5 and SHA-1 are fast but no longer considered secure against tampering; use " +
                "SHA-256 or stronger for anything security-sensitive, like verifying file integrity.",
        )
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.input,
            onValueChange = viewModel::onInputChange,
            placeholder = "Text to hash",
            minLines = 3,
            trailingContent = {
                com.xtech.xdevpocket.presentation.components.SecondaryActionButton(
                    text = "Paste",
                    onClick = { ClipboardHelper.pasteFromClipboard(context)?.let(viewModel::onInputChange) },
                )
            },
        )

        Spacer(Modifier.height(16.dp))
        SectionHeader("Algorithm")

        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            HashAlgorithm.entries.forEach { algorithm ->
                FilterChip(
                    selected = uiState.algorithm == algorithm,
                    onClick = { viewModel.onAlgorithmChange(algorithm) },
                    label = { Text(algorithm.label) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Aqua.copy(alpha = 0.18f),
                        selectedLabelColor = Aqua,
                    ),
                )
            }
        }

        if (!uiState.algorithm.recommended) {
            Spacer(Modifier.height(8.dp))
            Text(
                "${uiState.algorithm.label} is not recommended for security-sensitive use.",
                style = MaterialTheme.typography.bodySmall,
                color = Danger,
            )
        }

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Generate", onClick = viewModel::generate, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        CodeOutput(
            value = uiState.output,
            isError = uiState.isError,
            trailingContent = {
                Row {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "Hash output", uiState.output)
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
