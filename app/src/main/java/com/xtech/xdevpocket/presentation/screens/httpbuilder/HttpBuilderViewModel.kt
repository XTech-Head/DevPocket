package com.xtech.xdevpocket.presentation.screens.httpbuilder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.HttpHeader
import com.xtech.xdevpocket.domain.utilities.HttpMethod
import com.xtech.xdevpocket.domain.utilities.HttpRequestBuilderUtility
import com.xtech.xdevpocket.domain.utilities.HttpRequestSpec
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HttpBuilderUiState(
    val method: HttpMethod = HttpMethod.GET,
    val url: String = "",
    val headersText: String = "Content-Type: application/json",
    val body: String = "",
    val curlOutput: String = "",
    val rawOutput: String = "",
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class HttpBuilderViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HttpBuilderUiState())
    val uiState: StateFlow<HttpBuilderUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onMethodChange(method: HttpMethod) { _uiState.value = _uiState.value.copy(method = method) }
    fun onUrlChange(value: String) { _uiState.value = _uiState.value.copy(url = value) }
    fun onHeadersChange(value: String) { _uiState.value = _uiState.value.copy(headersText = value) }
    fun onBodyChange(value: String) { _uiState.value = _uiState.value.copy(body = value) }

    private fun parseHeaders(text: String): List<HttpHeader> =
        text.lines()
            .mapNotNull { line ->
                val idx = line.indexOf(':')
                if (idx <= 0) return@mapNotNull null
                HttpHeader(line.substring(0, idx).trim(), line.substring(idx + 1).trim())
            }

    fun build() {
        val state = _uiState.value
        val spec = HttpRequestSpec(
            method = state.method,
            url = state.url.trim(),
            headers = parseHeaders(state.headersText),
            body = state.body,
        )
        val curl = HttpRequestBuilderUtility.buildCurl(spec)
        val raw = HttpRequestBuilderUtility.buildRawHttp(spec)

        if (curl is TextOpResult.Error) {
            _uiState.value = state.copy(errorMessage = curl.message, curlOutput = "", rawOutput = "")
            return
        }

        val curlText = (curl as TextOpResult.Success).output
        val rawText = (raw as? TextOpResult.Success)?.output.orEmpty()

        _uiState.value = state.copy(curlOutput = curlText, rawOutput = rawText, errorMessage = null)

        if (state.saveHistory) {
            viewModelScope.launch {
                repository.addHistory("HTTP Request Builder", "${state.method} ${state.url}", curlText)
            }
        }
    }
}
