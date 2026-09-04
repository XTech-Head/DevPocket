package com.xtech.xdevpocket.presentation.screens.uuid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.UuidUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UuidUiState(
    val generatedIds: List<String> = listOf(UuidUtility.generate()),
    val count: Int = 1,
    val saveHistory: Boolean = true,
)

class UuidViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UuidUiState())
    val uiState: StateFlow<UuidUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onCountChange(count: Int) {
        _uiState.value = _uiState.value.copy(count = count.coerceIn(1, 50))
    }

    fun generate() {
        val state = _uiState.value
        val ids = UuidUtility.generateMultiple(state.count)
        _uiState.value = state.copy(generatedIds = ids)
        if (state.saveHistory) {
            viewModelScope.launch {
                repository.addHistory("UUID Generator", "count=${state.count}", ids.joinToString("\n"))
            }
        }
    }
}
