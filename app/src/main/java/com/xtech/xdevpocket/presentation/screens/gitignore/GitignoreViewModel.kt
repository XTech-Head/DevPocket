package com.xtech.xdevpocket.presentation.screens.gitignore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.GitignoreUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GitignoreUiState(
    val selectedIds: Set<String> = setOf("android", "kotlin_java_gradle"),
    val output: String = "",
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class GitignoreViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GitignoreUiState())
    val uiState: StateFlow<GitignoreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
        generate()
    }

    fun onToggleTemplate(id: String) {
        val current = _uiState.value.selectedIds
        val updated = if (current.contains(id)) current - id else current + id
        _uiState.value = _uiState.value.copy(selectedIds = updated)
        generate()
    }

    fun generate() {
        val state = _uiState.value
        when (val result = GitignoreUtility.generate(state.selectedIds.toList())) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, errorMessage = null)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory(
                            "Gitignore Generator",
                            state.selectedIds.joinToString(", "),
                            result.output,
                        )
                    }
                }
            }
            is TextOpResult.Error -> _uiState.value = state.copy(errorMessage = result.message, output = "")
        }
    }
}
