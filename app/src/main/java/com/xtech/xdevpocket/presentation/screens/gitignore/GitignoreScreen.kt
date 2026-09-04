package com.xtech.xdevpocket.presentation.screens.gitignore

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.utilities.GitignoreUtility
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
fun GitignoreScreen(
    viewModel: GitignoreViewModel,
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
            title = ".gitignore Generator",
            subtitle = "Combine templates into one file",
            onBack = onBack,
            helpText = "Pick every environment relevant to this repo — an Android app usually wants " +
                "Android + Kotlin/Gradle + your IDE. Selecting several templates merges them into one " +
                "file and drops duplicate lines automatically. Everything here is bundled in the app, " +
                "so it works offline.",
        )
        Spacer(Modifier.height(12.dp))

        SectionHeader("Templates")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            GitignoreUtility.templates.forEach { template ->
                val selected = uiState.selectedIds.contains(template.id)
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.onToggleTemplate(template.id) },
                    label = { androidx.compose.material3.Text(template.label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Aqua.copy(alpha = 0.18f),
                        selectedLabelColor = Aqua,
                    ),
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (uiState.errorMessage != null) {
            CodeOutput(value = uiState.errorMessage.orEmpty(), label = "Error", isError = true)
        } else {
            CodeOutput(
                value = uiState.output,
                label = ".gitignore",
                trailingContent = {
                    Row {
                        CopyButton(onClick = {
                            ClipboardHelper.copyToClipboard(context, ".gitignore", uiState.output)
                            scope.launch { snackbarHostState.showSnackbar("Copied") }
                        })
                        ShareButton(onClick = { ShareHelper.share(context, uiState.output) })
                    }
                },
            )
        }
    }
}
