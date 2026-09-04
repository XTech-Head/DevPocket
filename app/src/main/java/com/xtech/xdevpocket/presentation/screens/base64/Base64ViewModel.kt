package com.xtech.xdevpocket.presentation.screens.base64

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.Base64Utility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Base64UiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val saveHistory: Boolean = true,
    val clearAfterOperation: Boolean = false,
)

class Base64ViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(Base64UiState())
    val uiState: StateFlow<Base64UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(
                    saveHistory = prefs.saveHistory,
                    clearAfterOperation = prefs.clearAfterOperation,
                )
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun encode() = runOperation("Base64 Encode") { Base64Utility.encode(_uiState.value.input) }

    fun decode() = runOperation("Base64 Decode") { Base64Utility.decode(_uiState.value.input) }

    fun swap() {
        val state = _uiState.value
        if (state.output.isNotBlank() && !state.isError) {
            _uiState.value = state.copy(input = state.output, output = "")
        }
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(input = "", output = "", isError = false)
    }

    private fun runOperation(historyLabel: String, op: () -> TextOpResult) {
        val state = _uiState.value
        when (val result = op()) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch { repository.addHistory(historyLabel, state.input, result.output) }
                }
                if (state.clearAfterOperation) _uiState.value = _uiState.value.copy(input = "")
            }
            is TextOpResult.Error -> _uiState.value = state.copy(output = result.message, isError = true)
        }
    }
}
