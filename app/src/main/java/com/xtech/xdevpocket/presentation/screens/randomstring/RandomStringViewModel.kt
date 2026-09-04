package com.xtech.xdevpocket.presentation.screens.randomstring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.RandomStringUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RandomStringUiState(
    val length: Int = 16,
    val includeUpper: Boolean = true,
    val includeLower: Boolean = true,
    val includeDigits: Boolean = true,
    val includeSymbols: Boolean = false,
    val output: String = "",
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class RandomStringViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RandomStringUiState())
    val uiState: StateFlow<RandomStringUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
        generate()
    }

    fun onLengthChange(length: Int) {
        _uiState.value = _uiState.value.copy(length = length.coerceIn(1, 256))
    }

    fun onToggleUpper(v: Boolean) { _uiState.value = _uiState.value.copy(includeUpper = v) }
    fun onToggleLower(v: Boolean) { _uiState.value = _uiState.value.copy(includeLower = v) }
    fun onToggleDigits(v: Boolean) { _uiState.value = _uiState.value.copy(includeDigits = v) }
    fun onToggleSymbols(v: Boolean) { _uiState.value = _uiState.value.copy(includeSymbols = v) }

    fun generate() {
        val state = _uiState.value
        val options = RandomStringUtility.Options(
            length = state.length,
            includeUpper = state.includeUpper,
            includeLower = state.includeLower,
            includeDigits = state.includeDigits,
            includeSymbols = state.includeSymbols,
        )
        when (val result = RandomStringUtility.generate(options)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, errorMessage = null)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Random String", "length=${state.length}", result.output)
                    }
                }
            }
            is TextOpResult.Error -> _uiState.value = state.copy(errorMessage = result.message)
        }
    }
}
