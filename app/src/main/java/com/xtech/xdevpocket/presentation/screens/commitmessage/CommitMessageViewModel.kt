package com.xtech.xdevpocket.presentation.screens.commitmessage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.domain.utilities.CommitMessageUtility
import com.xtech.xdevpocket.domain.utilities.TextOpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CommitMessageUiState(
    val type: CommitMessageUtility.CommitType = CommitMessageUtility.CommitType.FEAT,
    val scope: String = "",
    val subject: String = "",
    val body: String = "",
    val breakingChange: String = "",
    val issueRefs: String = "",
    val output: String = "",
    val errorMessage: String? = null,
    val saveHistory: Boolean = true,
)

class CommitMessageViewModel(private val repository: DeveloperRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CommitMessageUiState())
    val uiState: StateFlow<CommitMessageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.preferences.collect { prefs ->
                _uiState.value = _uiState.value.copy(saveHistory = prefs.saveHistory)
            }
        }
    }

    fun onTypeChange(type: CommitMessageUtility.CommitType) {
        _uiState.value = _uiState.value.copy(type = type)
        build()
    }

    fun onScopeChange(v: String) { _uiState.value = _uiState.value.copy(scope = v); build() }
    fun onSubjectChange(v: String) { _uiState.value = _uiState.value.copy(subject = v); build() }
    fun onBodyChange(v: String) { _uiState.value = _uiState.value.copy(body = v); build() }
    fun onBreakingChangeChange(v: String) { _uiState.value = _uiState.value.copy(breakingChange = v); build() }
    fun onIssueRefsChange(v: String) { _uiState.value = _uiState.value.copy(issueRefs = v); build() }

    fun build() {
        val state = _uiState.value
        val options = CommitMessageUtility.Options(
            type = state.type,
            scope = state.scope,
            subject = state.subject,
            body = state.body,
            breakingChange = state.breakingChange,
            issueRefs = state.issueRefs,
        )
        when (val result = CommitMessageUtility.build(options)) {
            is TextOpResult.Success -> {
                _uiState.value = state.copy(output = result.output, errorMessage = null)
                if (state.saveHistory) {
                    viewModelScope.launch {
                        repository.addHistory("Commit Message Builder", state.subject, result.output)
                    }
                }
            }
            is TextOpResult.Error -> _uiState.value = state.copy(errorMessage = result.message, output = "")
        }
    }
}
