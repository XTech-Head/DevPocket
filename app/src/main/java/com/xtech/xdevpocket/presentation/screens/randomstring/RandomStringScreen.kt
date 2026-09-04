package com.xtech.xdevpocket.presentation.screens.randomstring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import kotlinx.coroutines.launch

@Composable
fun RandomStringScreen(
    viewModel: RandomStringViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        ToolHeader(title = "Random String", subtitle = "Generate secure random strings", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(onClick = { viewModel.onLengthChange(uiState.length - 4) }) {
                Icon(Icons.Filled.Remove, contentDescription = "Shorter", tint = Aqua)
            }
            Text(
                "${uiState.length} chars",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            IconButton(onClick = { viewModel.onLengthChange(uiState.length + 4) }) {
                Icon(Icons.Filled.Add, contentDescription = "Longer", tint = Aqua)
            }
        }

        Spacer(Modifier.height(8.dp))
        SectionHeader("Character Sets")
        OptionSwitchRow("Uppercase (A-Z)", uiState.includeUpper, viewModel::onToggleUpper)
        OptionSwitchRow("Lowercase (a-z)", uiState.includeLower, viewModel::onToggleLower)
        OptionSwitchRow("Digits (0-9)", uiState.includeDigits, viewModel::onToggleDigits)
        OptionSwitchRow("Symbols (!@#\$...)", uiState.includeSymbols, viewModel::onToggleSymbols)

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Generate", onClick = viewModel::generate, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else {
            CodeOutput(
                value = uiState.output,
                trailingContent = {
                    Row {
                        CopyButton(onClick = {
                            ClipboardHelper.copyToClipboard(context, "Random string", uiState.output)
                            scope.launch { snackbarHostState.showSnackbar("Copied") }
                        })
                        com.xtech.xdevpocket.presentation.components.ShareButton(onClick = {
                            com.xtech.xdevpocket.presentation.components.ShareHelper.share(context, uiState.output)
                        })
                    }
                },
            )
        }
    }
}

@Composable
private fun OptionSwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = Aqua),
        )
    }
}
