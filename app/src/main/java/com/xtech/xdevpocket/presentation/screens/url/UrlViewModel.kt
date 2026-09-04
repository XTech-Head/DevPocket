package com.xtech.xdevpocket.presentation.screens.url

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import com.xtech.xdevpocket.domain.utilities.UrlUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UrlUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val saveHistory: Boolean = true,
    val clearAfterOperation: Boolean = false,
)

class UrlViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UrlUiState())
    val uiState: StateFlow<UrlUiState> = _uiState.asStateFlow()

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

    fun encode() = runOperation("URL Encode") { UrlUtility.encode(_uiState.value.input) }

    fun decode() = runOperation("URL Decode") { UrlUtility.decode(_uiState.value.input) }

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
