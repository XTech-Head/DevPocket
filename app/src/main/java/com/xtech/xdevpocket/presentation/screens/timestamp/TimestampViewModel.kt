package com.xtech.xdevpocket.presentation.screens.timestamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import com.xtech.xdevpocket.domain.utilities.TimestampUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TimestampUiState(
    val timestampInput: String = "",
    val timestampOutput: String = "",
    val timestampError: Boolean = false,
    val dateInput: String = "",
    val dateOutput: String = "",
    val dateError: Boolean = false,
    val saveHistory: Boolean = true,
)

class TimestampViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TimestampUiState())
    val uiState: StateFlow<TimestampUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onTimestampInputChange(value: String) {
        _uiState.value = _uiState.value.copy(timestampInput = value)
    }

    fun onDateInputChange(value: String) {
        _uiState.value = _uiState.value.copy(dateInput = value)
    }

    fun useNow() {
        _uiState.value = _uiState.value.copy(timestampInput = TimestampUtility.nowUnixSeconds().toString())
    }

    fun convertToDate() {
        val state = _uiState.value
        when (val result = TimestampUtility.convertUnixToDate(state.timestampInput)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(timestampOutput = result.output, timestampError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Timestamp Converter", state.timestampInput, result.output)
                    }
                }
            }
            is TextOpResult.Error -> {
                _uiState.value = state.copy(timestampOutput = result.message, timestampError = true)
            }
        }
    }

    fun convertToUnix() {
        val state = _uiState.value
        when (val result = TimestampUtility.convertDateToUnix(state.dateInput)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(dateOutput = result.output, dateError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Timestamp Converter", state.dateInput, result.output)
                    }
                }
            }
            is TextOpResult.Error -> {
                _uiState.value = state.copy(dateOutput = result.message, dateError = true)
            }
        }
    }
}
