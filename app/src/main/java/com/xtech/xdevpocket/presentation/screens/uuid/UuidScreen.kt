package com.xtech.xdevpocket.presentation.screens.uuid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SecondaryActionButton
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import kotlinx.coroutines.launch

@Composable
fun UuidScreen(
    viewModel: UuidViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        ToolHeader(title = "UUID Generator", subtitle = "Generate UUID v4 values", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(onClick = { viewModel.onCountChange(uiState.count - 1) }) {
                Icon(Icons.Filled.Remove, contentDescription = "Fewer", tint = Aqua)
            }
            Text(
                "${uiState.count}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            IconButton(onClick = { viewModel.onCountChange(uiState.count + 1) }) {
                Icon(Icons.Filled.Add, contentDescription = "More", tint = Aqua)
            }
        }

        Spacer(Modifier.height(8.dp))
        PrimaryActionButton(text = "Generate", onClick = viewModel::generate, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(uiState.generatedIds) { id ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        id,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "UUID", id)
                        scope.launch { snackbarHostState.showSnackbar("Copied") }
                    }) {
                        Icon(
                            androidx.compose.material.icons.Icons.Filled.ContentCopy,
                            contentDescription = "Copy",
                            tint = Aqua,
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryActionButton(
                text = "Copy All",
                onClick = {
                    ClipboardHelper.copyToClipboard(context, "UUIDs", uiState.generatedIds.joinToString("\n"))
                    scope.launch { snackbarHostState.showSnackbar("Copied") }
                },
                modifier = Modifier.weight(1f),
            )
            com.xtech.xdevpocket.presentation.components.ShareButton(onClick = {
                com.xtech.xdevpocket.presentation.components.ShareHelper.share(
                    context,
                    uiState.generatedIds.joinToString("\n"),
                )
            })
        }
    }
}
