package com.xtech.xdevpocket.presentation.screens.regex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.utilities.RegexUtility
import com.xtech.xdevpocket.presentation.components.CodeInput
import com.xtech.xdevpocket.presentation.components.PrimaryActionButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua

@Composable
fun RegexScreen(
    viewModel: RegexViewModel,
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
        ToolHeader(
            title = "Regex Tester",
            subtitle = "Test patterns live",
            onBack = onBack,
            helpText = "Write a regular expression and test text, then tap Test. Matches are listed " +
                "with their position in the text. If your pattern has capture groups in parentheses, " +
                "e.g. (\\d+), the captured values show up under each match. New to regex? Try the " +
                "example chips below the pattern field.",
        )
        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.pattern,
            onValueChange = viewModel::onPatternChange,
            label = "Regular Expression",
            placeholder = "^[A-Za-z0-9]+$",
            minLines = 2,
        )

        Spacer(Modifier.height(12.dp))

        CodeInput(
            value = uiState.testText,
            onValueChange = viewModel::onTestTextChange,
            label = "Test Text",
            placeholder = "Paste text to test against the pattern",
            minLines = 5,
        )

        Spacer(Modifier.height(12.dp))
        SectionHeader("Examples")
        Row(modifier = Modifier.fillMaxWidth()) {
            RegexUtility.examples.take(2).forEach { (pattern, label) ->
                AssistChip(
                    onClick = { viewModel.applyExample(pattern) },
                    label = { Text(label) },
                    modifier = Modifier.padding(end = 8.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        PrimaryActionButton(text = "Test", onClick = viewModel::test, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            Text(
                uiState.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            Text(
                "${uiState.matches.size} match${if (uiState.matches.size == 1) "" else "es"}",
                style = MaterialTheme.typography.titleMedium,
                color = Aqua,
            )
            Spacer(Modifier.height(8.dp))
            uiState.matches.forEachIndexed { index, match ->
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Text(
                        "Match ${index + 1}: \"${match.fullMatch}\" (${match.range.first}-${match.range.last})",
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    if (match.groups.isNotEmpty()) {
                        Text(
                            "Groups: ${match.groups.joinToString(", ")}",
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
