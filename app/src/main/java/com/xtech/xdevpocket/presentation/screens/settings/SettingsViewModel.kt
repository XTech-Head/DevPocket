package com.xtech.xdevpocket.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xtech.xdevpocket.data.preferences.AppTheme
import com.xtech.xdevpocket.data.preferences.UserPreferences
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: DeveloperRepository) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = repository.preferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { repository.setTheme(theme) }
    }

    fun setSaveHistory(enabled: Boolean) {
        viewModelScope.launch { repository.setSaveHistory(enabled) }
    }

    fun setAutoCopy(enabled: Boolean) {
        viewModelScope.launch { repository.setAutoCopy(enabled) }
    }

    fun setClearAfterOperation(enabled: Boolean) {
        viewModelScope.launch { repository.setClearAfterOperation(enabled) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearHistory() }
    }

    fun clearFavorites() {
        viewModelScope.launch { repository.clearFavorites() }
    }
}
