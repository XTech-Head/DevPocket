package com.xtech.xdevpocket.presentation.screens.hash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.HashAlgorithm
import com.xtech.xdevpocket.domain.utilities.HashUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HashUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val algorithm: HashAlgorithm = HashAlgorithm.SHA256,
    val saveHistory: Boolean = true,
)

class HashViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HashUiState())
    val uiState: StateFlow<HashUiState> = _uiState.asStateFlow()

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

    fun onAlgorithmChange(algorithm: HashAlgorithm) {
        _uiState.value = _uiState.value.copy(algorithm = algorithm)
    }

    fun generate() {
        val state = _uiState.value
        when (val result = HashUtility.hash(state.input, state.algorithm)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory(state.algorithm.label, state.input, result.output)
                    }
                }
            }
            is TextOpResult.Error -> _uiState.value = state.copy(output = result.message, isError = true)
        }
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(input = "", output = "", isError = false)
    }
}
