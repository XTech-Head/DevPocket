package com.xtech.xdevpocket.presentation.screens.colorconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.ColorConversion
import com.xtech.xdevpocket.domain.utilities.ColorConverterUtility
import com.xtech.xdevpocket.domain.utilities.ColorResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ColorConverterUiState(
    val input: String = "",
    val conversion: ColorConversion? = null,
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class ColorConverterViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ColorConverterUiState())
    val uiState: StateFlow<ColorConverterUiState> = _uiState.asStateFlow()

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

    fun convert() {
        val state = _uiState.value
        when (val result = ColorConverterUtility.convert(state.input)) {
            is ColorResult.Success -> {
                _uiState.value = state.copy(conversion = result.conversion, errorMessage = null)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Color Converter", state.input, result.conversion.hex)
                    }
                }
            }
            is ColorResult.Error -> {
                _uiState.value = state.copy(conversion = null, errorMessage = result.message)
            }
        }
    }

    fun clear() {
        _uiState.value = ColorConverterUiState(saveHistory = _uiState.value.saveHistory)
    }
}
