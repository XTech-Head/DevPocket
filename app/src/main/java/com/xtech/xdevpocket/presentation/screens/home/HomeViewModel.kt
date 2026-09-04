package com.xtech.xdevpocket.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.local.HistoryEntity
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.model.Tool
import com.xtech.xdevpocket.domain.model.Tools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val filteredTools: List<Tool> = Tools.all,
    val favoriteToolIds: Set<String> = emptySet(),
    val recentTools: List<Tool> = emptyList(),
)

class HomeViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        searchQuery,
        repository.observeFavorites(),
        repository.observeHistory(),
    ) { query, favorites, history ->
        val favoriteIds = favorites.map { it.toolId }.toSet()
        val recentToolIds = history.map { it.tool }.distinct().take(4)
        val recentTools = recentToolIds.mapNotNull { toolTitle ->
            Tools.all.find { it.title == toolTitle }
        }
        HomeUiState(
            searchQuery = query,
            filteredTools = Tools.search(query),
            favoriteToolIds = favoriteIds,
            recentTools = recentTools,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onToggleFavorite(toolId: String) {
        viewModelScope.launch {
            val isFav = uiState.value.favoriteToolIds.contains(toolId)
            repository.toggleFavorite(toolId, isFav)
        }
    }
}
