package com.xtech.xdevpocket.presentation.screens.httpbuilder

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
import com.xtech.xdevpocket.domain.utilities.HttpMethod
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ShareButton
import com.xtech.xdevpocket.presentation.components.ShareHelper
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import kotlinx.coroutines.launch

@Composable
fun HttpBuilderScreen(
    viewModel: HttpBuilderViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()),
    ) {
        ToolHeader(
            title = "HTTP Request Builder",
            subtitle = "Build curl & raw requests — offline",
            onBack = onBack,
            helpText = "Fill in a method, URL, headers (one per line as Key: Value), and an optional " +
                "body, then tap Build. This only formats the request as text — it never actually " +
                "sends anything over the network.",
        )
        Spacer(Modifier.height(12.dp))

        SectionHeader("Method")
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            HttpMethod.entries.forEach { method ->
                FilterChip(
                    selected = uiState.method == method,
                    onClick = { viewModel.onMethodChange(method) },
                    label = { Text(method.name) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Aqua.copy(alpha = 0.18f),
                        selectedLabelColor = Aqua,
                    ),
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        CodeInput(
            value = uiState.url,
            onValueChange = viewModel::onUrlChange,
            label = "URL",
            placeholder = "https://api.example.com/v1/users",
            minLines = 1,
        )

        Spacer(Modifier.height(12.dp))
        CodeInput(
            value = uiState.headersText,
            onValueChange = viewModel::onHeadersChange,
            label = "Headers (one per line)",
            placeholder = "Authorization: Bearer <token>",
            minLines = 3,
        )

        Spacer(Modifier.height(12.dp))
        CodeInput(
            value = uiState.body,
            onValueChange = viewModel::onBodyChange,
            label = "Body (JSON, form data, etc.)",
            placeholder = "{\"key\":\"value\"}",
            minLines = 4,
        )

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Build", onClick = viewModel::build, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else if (uiState.curlOutput.isNotBlank()) {
            CodeOutput(
                value = uiState.curlOutput,
                label = "curl",
                trailingContent = {
                    Row {
                        CopyButton(onClick = {
                            ClipboardHelper.copyToClipboard(context, "curl command", uiState.curlOutput)
                            scope.launch { snackbarHostState.showSnackbar("Copied") }
                        })
                        ShareButton(onClick = { ShareHelper.share(context, uiState.curlOutput) })
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            CodeOutput(
                value = uiState.rawOutput,
                label = "Raw HTTP",
                trailingContent = {
                    Row {
                        CopyButton(onClick = {
                            ClipboardHelper.copyToClipboard(context, "Raw HTTP request", uiState.rawOutput)
                            scope.launch { snackbarHostState.showSnackbar("Copied") }
                        })
                        ShareButton(onClick = { ShareHelper.share(context, uiState.rawOutput) })
                    }
                },
            )
        }
    }
}
