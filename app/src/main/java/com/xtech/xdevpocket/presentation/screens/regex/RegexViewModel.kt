package com.xtech.xdevpocket.presentation.screens.regex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.RegexMatchResult
import com.xtech.xdevpocket.domain.utilities.RegexTestResult
import com.xtech.xdevpocket.domain.utilities.RegexUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegexUiState(
    val pattern: String = "",
    val testText: String = "",
    val matches: List<RegexMatchResult> = emptyList(),
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class RegexViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegexUiState())
    val uiState: StateFlow<RegexUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onPatternChange(value: String) {
        _uiState.value = _uiState.value.copy(pattern = value)
    }

    fun onTestTextChange(value: String) {
        _uiState.value = _uiState.value.copy(testText = value)
    }

    fun test() {
        val state = _uiState.value
        when (val result = RegexUtility.test(state.pattern, state.testText)) {
            is RegexTestResult.Success -> {
                _uiState.value = state.copy(matches = result.matches, errorMessage = null)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Regex Tester", state.pattern, "${result.matches.size} matches")
                    }
                }
            }
            is RegexTestResult.Error -> {
                _uiState.value = state.copy(matches = emptyList(), errorMessage = result.message)
            }
        }
    }

    fun applyExample(pattern: String) {
        _uiState.value = _uiState.value.copy(pattern = pattern)
    }
}
