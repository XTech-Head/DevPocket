package com.xtech.xdevpocket.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.local.HistoryEntity
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HistoryUiState(
    val entries: List<HistoryEntity> = emptyList(),
    val searchQuery: String = "",
)

class HistoryViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val uiState: StateFlow<HistoryUiState> = combine(
        repository.observeHistory(),
        searchQuery,
    ) { entries, query ->
        val filtered = if (query.isBlank()) {
            entries
        } else {
            entries.filter {
                it.tool.contains(query, ignoreCase = true) ||
                    it.input.contains(query, ignoreCase = true) ||
                    it.output.contains(query, ignoreCase = true)
            }
        }
        HistoryUiState(entries = filtered, searchQuery = query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUiState())

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun deleteEntry(entity: HistoryEntity) {
        viewModelScope.launch { repository.deleteHistory(entity) }
    }

    fun clearAll() {
        viewModelScope.launch { repository.clearHistory() }
    }
}
