package com.xtech.xdevpocket.presentation.screens.caseconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.CaseConverterUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CaseConverterUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val selectedCase: CaseConverterUtility.CaseType = CaseConverterUtility.CaseType.CAMEL,
    val saveHistory: Boolean = true,
)

class CaseConverterViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CaseConverterUiState())
    val uiState: StateFlow<CaseConverterUiState> = _uiState.asStateFlow()

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

    fun onCaseTypeChange(type: CaseConverterUtility.CaseType) {
        _uiState.value = _uiState.value.copy(selectedCase = type)
    }

    fun convert() {
        val state = _uiState.value
        when (val result = CaseConverterUtility.convert(state.input, state.selectedCase)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, isError = false)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Case Converter (${state.selectedCase.label})", state.input, result.output)
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
