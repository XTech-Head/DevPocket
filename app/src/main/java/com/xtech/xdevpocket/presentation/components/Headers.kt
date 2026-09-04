package com.xtech.xdevpocket.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.theme.Aqua

/**
 * Standard screen header. When [helpText] is provided, a small info icon appears
 * that opens a one-off explanation dialog — meant for tools whose purpose or
 * output isn't self-evident to a beginner (JWT claims, regex groups, cron fields).
 * This is intentionally not an onboarding flow: it's opt-in, per-screen, and never
 * shown automatically.
 */
@Composable
fun ToolHeader(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    helpText: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    var showHelp by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
            androidx.compose.foundation.layout.Column {
                Text(title, style = MaterialTheme.typography.headlineMedium)
                if (subtitle != null) {
                    Text(
                        subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (helpText != null) {
                IconButton(onClick = { showHelp = true }) {
                    Icon(Icons.Filled.HelpOutline, contentDescription = "About this tool", tint = Aqua)
                }
            }
        }
        trailingContent?.invoke()
    }

    if (showHelp && helpText != null) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) { Text("Got it", color = Aqua) }
            },
            title = { Text(title) },
            text = { Text(helpText, style = MaterialTheme.typography.bodyMedium) },
        )
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(vertical = 8.dp),
    )
}
