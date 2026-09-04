package com.xtech.xdevpocket.presentation.screens.xml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import com.xtech.xdevpocket.domain.utilities.XmlFormatterUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class XmlUiState(
    val input: String = "",
    val output: String = "",
    val isError: Boolean = false,
    val saveHistory: Boolean = true,
)

class XmlViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(XmlUiState())
    val uiState: StateFlow<XmlUiState> = _uiState.asStateFlow()

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

    fun format() = runOp("XML Formatter") { XmlFormatterUtility.format(_uiState.value.input) }
    fun minify() = runOp("XML Minifier") { XmlFormatterUtility.minify(_uiState.value.input) }
    fun validate() = runOp("XML Validator") { XmlFormatterUtility.validate(_uiState.value.input) }

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
