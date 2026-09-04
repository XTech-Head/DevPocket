package com.xtech.xdevpocket.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.presentation.components.EmptyState
import com.xtech.xdevpocket.presentation.components.HistoryItem
import com.xtech.xdevpocket.presentation.components.ToolSearchBar
import com.xtech.xdevpocket.presentation.theme.Danger
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onExploreTools: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("History", style = MaterialTheme.typography.headlineLarge)
            if (uiState.entries.isNotEmpty()) {
                TextButton(onClick = viewModel::clearAll) {
                    Text("Delete all", color = Danger)
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        if (uiState.entries.isEmpty() && uiState.searchQuery.isBlank()) {
            EmptyState(
                icon = Icons.Filled.History,
                title = "No history yet",
                message = "Your developer operations will appear here.",
                actionLabel = "Explore Tools",
                onAction = onExploreTools,
            )
        } else {
            ToolSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Search history...",
            )
            Spacer(Modifier.height(12.dp))

            if (uiState.entries.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.History,
                    title = "No matches",
                    message = "Try a different search term.",
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                ) {
                    items(uiState.entries, key = { it.id }) { entry ->
                        HistoryItem(
                            toolName = entry.tool,
                            preview = entry.output.take(80).replace("\n", " "),
                            timeAgo = timeAgo(entry.timestamp),
                            onClick = {},
                            onDelete = { viewModel.deleteEntry(entry) },
                        )
                    }
                }
            }
        }
    }
}

private fun timeAgo(timestamp: Long): String {
    val diffMillis = System.currentTimeMillis() - timestamp
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
        hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
        days < 7 -> "$days day${if (days == 1L) "" else "s"} ago"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(java.util.Date(timestamp))
    }
}
