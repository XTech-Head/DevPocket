package com.xtech.xdevpocket.presentation.screens.url

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.ClearButton
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SecondaryActionButton
import com.xtech.xdevpocket.presentation.components.ToolHeader
import kotlinx.coroutines.launch

@Composable
fun UrlScreen(
    viewModel: UrlViewModel,
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
        ToolHeader(title = "URL Encoder", subtitle = "Encode & decode URLs", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.input,
            onValueChange = viewModel::onInputChange,
            placeholder = "https://example.com/?q=hello world",
            trailingContent = {
                SecondaryActionButton(
                    text = "Paste",
                    onClick = { ClipboardHelper.pasteFromClipboard(context)?.let(viewModel::onInputChange) },
                )
            },
        )

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PrimaryActionButton(text = "Encode", onClick = viewModel::encode, modifier = Modifier.weight(1f))
            PrimaryActionButton(text = "Decode", onClick = viewModel::decode, modifier = Modifier.weight(1f))
            SecondaryActionButton(text = "Swap", onClick = viewModel::swap, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(20.dp))

        CodeOutput(
            value = uiState.output,
            isError = uiState.isError,
            trailingContent = {
                Row {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "URL output", uiState.output)
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
