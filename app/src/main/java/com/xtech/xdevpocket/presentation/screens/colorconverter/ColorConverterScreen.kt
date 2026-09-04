package com.xtech.xdevpocket.presentation.screens.colorconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ColorConverterScreen(
    viewModel: ColorConverterViewModel,
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        ToolHeader(title = "Color Converter", subtitle = "Hex, RGB & HSL", onBack = onBack)
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.input,
            onValueChange = viewModel::onInputChange,
            placeholder = "#00D9C0, rgb(0,217,192), or hsl(174,100%,42%)",
            minLines = 1,
            trailingContent = {
                SecondaryActionButton(
                    text = "Paste",
                    onClick = { ClipboardHelper.pasteFromClipboard(context)?.let(viewModel::onInputChange) },
                )
            },
        )

        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            PrimaryActionButton(text = "Convert", onClick = viewModel::convert, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            ClearButton(onClick = viewModel::clear)
        }

        Spacer(Modifier.height(20.dp))

        val conversion = uiState.conversion
        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else if (conversion != null) {
            val parsedColor = runCatching {
                Color(android.graphics.Color.parseColor(conversion.hex))
            }.getOrNull()

            if (parsedColor != null) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(80.dp)
                        .background(parsedColor, RoundedCornerShape(14.dp)),
                )
                Spacer(Modifier.height(16.dp))
            }

            CodeOutput(
                value = conversion.hex,
                label = "Hex",
                trailingContent = {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "Hex", conversion.hex)
                        scope.launch { snackbarHostState.showSnackbar("Copied") }
                    })
                },
            )
            Spacer(Modifier.height(12.dp))
            CodeOutput(
                value = conversion.rgb,
                label = "RGB",
                trailingContent = {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "RGB", conversion.rgb)
                        scope.launch { snackbarHostState.showSnackbar("Copied") }
                    })
                },
            )
            Spacer(Modifier.height(12.dp))
            CodeOutput(
                value = conversion.hsl,
                label = "HSL",
                trailingContent = {
                    CopyButton(onClick = {
                        ClipboardHelper.copyToClipboard(context, "HSL", conversion.hsl)
                        scope.launch { snackbarHostState.showSnackbar("Copied") }
                    })
                },
            )
            Spacer(Modifier.height(16.dp))
            com.xtech.xdevpocket.presentation.components.ShareButton(
                onClick = {
                    com.xtech.xdevpocket.presentation.components.ShareHelper.share(
                        context,
                        "Hex: ${conversion.hex}\nRGB: ${conversion.rgb}\nHSL: ${conversion.hsl}",
                    )
                },
            )
        }
    }
}
