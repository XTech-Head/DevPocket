package com.xtech.xdevpocket.presentation.screens.commitmessage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.utilities.CommitMessageUtility
import com.xtech.xdevpocket.presentation.components.ClipboardHelper
import com.xtech.xdevpocket.presentation.components.CodeOutput
import com.xtech.xdevpocket.presentation.components.CopyButton
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ShareButton
import com.xtech.xdevpocket.presentation.components.ShareHelper
import com.xtech.xdevpocket.presentation.components.ToolHeader
import com.xtech.xdevpocket.presentation.theme.Aqua
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommitMessageScreen(
    viewModel: CommitMessageViewModel,
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
            title = "Commit Message Builder",
            subtitle = "Conventional Commits format",
            onBack = onBack,
            helpText = "Conventional Commits (type(scope): subject) makes history readable and lets " +
                "tools auto-generate changelogs and version bumps. Use \"!\" or fill in Breaking Change " +
                "for changes that break compatibility — that's what tells semantic-release to cut a " +
                "major version.",
        )
        Spacer(Modifier.height(12.dp))

        SectionHeader("Type")
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            CommitMessageUtility.CommitType.entries.forEach { type ->
                FilterChip(
                    selected = uiState.type == type,
                    onClick = { viewModel.onTypeChange(type) },
                    label = { Text(type.prefix) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Aqua.copy(alpha = 0.18f),
                        selectedLabelColor = Aqua,
                    ),
                )
            }
        }
        Text(
            uiState.type.label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(16.dp))
        LabeledField(
            label = "Scope (optional)",
            value = uiState.scope,
            onValueChange = viewModel::onScopeChange,
            placeholder = "e.g. jwt, ui, api",
        )

        Spacer(Modifier.height(12.dp))
        LabeledField(
            label = "Subject",
            value = uiState.subject,
            onValueChange = viewModel::onSubjectChange,
            placeholder = "short summary, imperative mood",
        )

        Spacer(Modifier.height(12.dp))
        LabeledField(
            label = "Body (optional)",
            value = uiState.body,
            onValueChange = viewModel::onBodyChange,
            placeholder = "what changed and why",
            minLines = 3,
        )

        Spacer(Modifier.height(12.dp))
        LabeledField(
            label = "Breaking change (optional)",
            value = uiState.breakingChange,
            onValueChange = viewModel::onBreakingChangeChange,
            placeholder = "describe the incompatibility, if any",
        )

        Spacer(Modifier.height(12.dp))
        LabeledField(
            label = "Issue refs (optional)",
            value = uiState.issueRefs,
            onValueChange = viewModel::onIssueRefsChange,
            placeholder = "Closes #42",
        )

        Spacer(Modifier.height(20.dp))

        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else {
            CodeOutput(
                value = uiState.output,
                label = "Commit message",
                trailingContent = {
                    Row {
                        CopyButton(onClick = {
                            ClipboardHelper.copyToClipboard(context, "Commit message", uiState.output)
                            scope.launch { snackbarHostState.showSnackbar("Copied") }
                        })
                        ShareButton(onClick = { ShareHelper.share(context, uiState.output) })
                    }
                },
            )
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            minLines = minLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Aqua,
                cursorColor = Aqua,
            ),
        )
    }
}
