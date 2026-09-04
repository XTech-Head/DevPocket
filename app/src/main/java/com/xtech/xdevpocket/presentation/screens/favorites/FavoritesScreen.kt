package com.xtech.xdevpocket.presentation.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.domain.model.Tool
import com.xtech.xdevpocket.presentation.components.DevToolCard
import com.xtech.xdevpocket.presentation.components.EmptyState

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onToolClick: (Tool) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Favorites", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(12.dp))

        if (uiState.favoriteTools.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.StarBorder,
                title = "No favorites yet",
                message = "Star a tool from Home to pin it here.",
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(uiState.favoriteTools, key = { it.id }) { tool ->
                    DevToolCard(
                        title = tool.title,
                        subtitle = tool.subtitle,
                        icon = tool.icon,
                        isFavorite = true,
                        onClick = { onToolClick(tool) },
                        onFavoriteClick = { viewModel.removeFavorite(tool.id) },
                    )
                }
            }
        }
    }
}
