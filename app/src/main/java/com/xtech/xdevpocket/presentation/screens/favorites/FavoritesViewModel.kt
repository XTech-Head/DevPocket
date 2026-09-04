package com.xtech.xdevpocket.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.model.Tool
import com.xtech.xdevpocket.domain.model.Tools
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritesUiState(val favoriteTools: List<Tool> = emptyList())

class FavoritesViewModel(private val repository: DeveloperRepository) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = repository.observeFavorites()
        .map { favorites ->
            val ids = favorites.map { it.toolId }
            FavoritesUiState(favoriteTools = ids.mapNotNull { Tools.byId(it) })
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoritesUiState())

    fun removeFavorite(toolId: String) {
        viewModelScope.launch { repository.toggleFavorite(toolId, currentlyFavorite = true) }
    }
}
