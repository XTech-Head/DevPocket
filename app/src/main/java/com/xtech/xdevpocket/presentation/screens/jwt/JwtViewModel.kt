package com.xtech.xdevpocket.presentation.screens.jwt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.JwtResult
import com.xtech.xdevpocket.domain.utilities.JwtUtility
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JwtUiState(
    val input: String = "",
    val headerJson: String = "",
    val payloadJson: String = "",
    val algorithm: String? = null,
    val tokenType: String? = null,
    val issuedAt: String? = null,
    val expiresAt: String? = null,
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class JwtViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(JwtUiState())
    val uiState: StateFlow<JwtUiState> = _uiState.asStateFlow()

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

    fun decode() {
        val state = _uiState.value
        when (val result = JwtUtility.decode(state.input)) {
            is JwtResult.Success -> {
                _uiState.value = state.copy(
                    headerJson = result.decoded.headerJson,
                    payloadJson = result.decoded.payloadJson,
                    algorithm = result.decoded.algorithm,
                    tokenType = result.decoded.tokenType,
                    issuedAt = result.decoded.issuedAt,
                    expiresAt = result.decoded.expiresAt,
                    errorMessage = null,
                )
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("JWT Decoder", state.input, result.decoded.payloadJson)
                    }
                }
            }
            is JwtResult.Error -> {
                _uiState.value = state.copy(
                    headerJson = "",
                    payloadJson = "",
                    algorithm = null,
                    tokenType = null,
                    issuedAt = null,
                    expiresAt = null,
                    errorMessage = result.message,
                )
            }
        }
    }

    fun clear() {
        _uiState.value = JwtUiState(saveHistory = _uiState.value.saveHistory)
    }
}
