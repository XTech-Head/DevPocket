package com.xtech.xdevpocket.presentation.screens.json

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.JsonFormatter
import com.xtech.xdevpocket.domain.utilities.JsonResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JsonUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val autoCopy: Boolean = false,
    val clearAfterOperation: Boolean = false,
    val saveHistory: Boolean = true,
)

class JsonViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(JsonUiState())
    val uiState: StateFlow<JsonUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(
                    autoCopy = prefs.autoCopy,
                    clearAfterOperation = prefs.clearAfterOperation,
                    saveHistory = prefs.saveHistory,
                )
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun format() = runOperation("JSON Formatter") { JsonFormatter.format(_uiState.value.input) }

    fun minify() = runOperation("JSON Minifier") { JsonFormatter.minify(_uiState.value.input) }

    fun validate() = runOperation("JSON Validator") { JsonFormatter.validate(_uiState.value.input) }

    fun clear() {
        _uiState.value = _uiState.value.copy(input = "", output = "", isError = false)
    }

    private fun runOperation(historyLabel: String, op: () -> JsonResult) {
        val result = op()
        val state = _uiState.value
        when (result) {
            is JsonResult.Success -> {
                _uiState.value = state.copy(output = result.output, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory(historyLabel, state.input, result.output)
                    }
                }
                if (state.clearAfterOperation) {
                    _uiState.value = _uiState.value.copy(input = "")
                }
            }
            is JsonResult.Error -> {
                _uiState.value = state.copy(output = result.message, isError = true)
            }
        }
    }
}
