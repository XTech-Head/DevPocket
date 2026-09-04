package com.xtech.xdevpocket.presentation.screens.jwt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.ClearButton
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SecondaryActionButton
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua

@Composable
fun JwtScreen(
    viewModel: JwtViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        ToolHeader(
            title = "JWT Decoder",
            subtitle = "Decode only — no signature verification",
            onBack = onBack,
            helpText = "A JWT (JSON Web Token) has three dot-separated parts: header, payload, and " +
                "signature. This tool decodes the header and payload so you can read the claims — " +
                "it does not verify the signature, so it can't tell you whether the token is authentic " +
                "or has expired securely. Never trust a decoded-only token for authorization decisions.",
        )
        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = Aqua, modifier = Modifier.padding(end = 6.dp))
            Text(
                "Your token stays on this device. Signature verification is not performed.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.input,
            onValueChange = viewModel::onInputChange,
            placeholder = "eyJhbGciOiJIUzI1NiJ9...",
            minLines = 4,
            trailingContent = {
                SecondaryActionButton(
                    text = "Paste",
                    onClick = { ClipboardHelper.pasteFromClipboard(context)?.let(viewModel::onInputChange) },
                )
            },
        )

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            PrimaryActionButton(text = "Decode", onClick = viewModel::decode, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            ClearButton(onClick = viewModel::clear)
        }

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else if (uiState.headerJson.isNotBlank()) {
            if (uiState.algorithm != null || uiState.tokenType != null) {
                Text(
                    "Algorithm: ${uiState.algorithm ?: "—"}   Type: ${uiState.tokenType ?: "—"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (uiState.issuedAt != null) {
                Text(
                    "Issued at: ${uiState.issuedAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (uiState.expiresAt != null) {
                Text(
                    "Expires at: ${uiState.expiresAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(12.dp))
            CodeOutput(value = uiState.headerJson, label = "Header")
            Spacer(Modifier.height(12.dp))
            CodeOutput(
                value = uiState.payloadJson,
                label = "Payload",
                trailingContent = {
                    com.xtech.xdevpocket.presentation.components.ShareButton(onClick = {
                        com.xtech.xdevpocket.presentation.components.ShareHelper.share(context, uiState.payloadJson)
                    })
                },
            )
        }
    }
}
