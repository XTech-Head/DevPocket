package com.xtech.xdevpocket.presentation.screens.cron

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.CronResult
import com.xtech.xdevpocket.domain.utilities.CronUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CronUiState(
    val expression: String = "",
    val description: String = "",
    val isError: Boolean = false,
    val saveHistory: Boolean = true,
)

class CronViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CronUiState())
    val uiState: StateFlow<CronUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onExpressionChange(value: String) {
        _uiState.value = _uiState.value.copy(expression = value)
    }

    fun applyExample(expression: String) {
        _uiState.value = _uiState.value.copy(expression = expression)
    }

    fun describe() {
        val state = _uiState.value
        when (val result = CronUtility.describe(state.expression)) {
            is CronResult.Success -> {
                _uiState.value = state.copy(description = result.description, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Cron Helper", state.expression, result.description)
                    }
                }
            }
            is CronResult.Error -> _uiState.value = state.copy(description = result.message, isError = true)
        }
    }

    companion object {
        val examples = listOf(
            "*/5 * * * *" to "Every 5 minutes",
            "0 9 * * 1" to "9 AM every Monday",
            "0 0 1 * *" to "Midnight on the 1st",
        )
    }
}
