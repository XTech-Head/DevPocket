package com.xtech.xdevpocket.presentation.screens.sql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.SqlFormatterUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SqlUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val saveHistory: Boolean = true,
)

class SqlViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SqlUiState())
    val uiState: StateFlow<SqlUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onInputChange(value: String) {
        _uiState.value = _uiState.value.copy(input = value)
    }

    fun format() = runOp("SQL Formatter") { SqlFormatterUtility.format(_uiState.value.input) }
    fun minify() = runOp("SQL Minifier") { SqlFormatterUtility.minify(_uiState.value.input) }

    fun clear() {
        _uiState.value = _uiState.value.copy(input = "", output = "", isError = false)
    }

    private fun runOp(label: String, op: () -> TextOpResult) {
        val state = _uiState.value
        when (val result = op()) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch { repository.addHistory(label, state.input, result.output) }
                }
            }
            is TextOpResult.Error -> _uiState.value = state.copy(output = result.message, isError = true)
        }
    }
}
