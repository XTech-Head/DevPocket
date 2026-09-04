package com.xtech.xdevpocket.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.R
import com.xtech.xdevpocket.domain.model.Tool
import com.xtech.xdevpocket.domain.model.ToolCategory
import com.xtech.xdevpocket.presentation.components.DevToolCard
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.ToolSearchBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onToolClick: (Tool) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.logo_xdevpocket),
                    contentDescription = "x-DevPocket",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(88.dp),
                )
                androidx.compose.foundation.layout.Spacer(Modifier.height(16.dp))
                ToolSearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                )
                androidx.compose.foundation.layout.Spacer(Modifier.height(12.dp))
            }
        }

        if (uiState.searchQuery.isBlank()) {
            if (uiState.recentTools.isNotEmpty()) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    SectionHeader("Recently Used")
                }
                items(uiState.recentTools, key = { "recent_${it.id}" }) { tool ->
                    DevToolCard(
                        title = tool.title,
                        subtitle = tool.subtitle,
                        icon = tool.icon,
                        isFavorite = uiState.favoriteToolIds.contains(tool.id),
                        onClick = { onToolClick(tool) },
                        onFavoriteClick = { viewModel.onToggleFavorite(tool.id) },
                    )
                }
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                SectionHeader("All Tools")
            }

            ToolCategory.entries.forEach { category ->
                val toolsInCategory = uiState.filteredTools.filter { it.category == category }
                if (toolsInCategory.isNotEmpty()) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        )
                    }
                    items(toolsInCategory, key = { it.id }) { tool ->
                        DevToolCard(
                            title = tool.title,
                            subtitle = tool.subtitle,
                            icon = tool.icon,
                            isFavorite = uiState.favoriteToolIds.contains(tool.id),
                            onClick = { onToolClick(tool) },
                            onFavoriteClick = { viewModel.onToggleFavorite(tool.id) },
                        )
                    }
                }
            }
        } else {
            if (uiState.filteredTools.isEmpty()) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    com.xtech.xdevpocket.presentation.components.EmptyState(
                        icon = androidx.compose.material.icons.Icons.Filled.Search,
                        title = "No tools found",
                        message = "Try searching for JSON, Hash, UUID, or Regex.",
                    )
                }
            } else {
                items(uiState.filteredTools, key = { it.id }) { tool ->
                    DevToolCard(
                        title = tool.title,
                        subtitle = tool.subtitle,
                        icon = tool.icon,
                        isFavorite = uiState.favoriteToolIds.contains(tool.id),
                        onClick = { onToolClick(tool) },
                        onFavoriteClick = { viewModel.onToggleFavorite(tool.id) },
                    )
                }
            }
        }
    }
}
