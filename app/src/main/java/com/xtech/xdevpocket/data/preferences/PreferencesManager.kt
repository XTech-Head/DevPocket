package com.xtech.xdevpocket.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "x_devpocket_prefs")

enum class AppTheme { DARK, LIGHT, SYSTEM }

data class UserPreferences(
    val theme: AppTheme = AppTheme.DARK,
    val saveHistory: Boolean = true,
    val autoCopy: Boolean = false,
    val clearAfterOperation: Boolean = false,
)

class PreferencesManager(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val SAVE_HISTORY = booleanPreferencesKey("save_history")
        val AUTO_COPY = booleanPreferencesKey("auto_copy")
        val CLEAR_AFTER_OP = booleanPreferencesKey("clear_after_operation")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            theme = prefs[Keys.THEME]?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?: AppTheme.DARK,
            saveHistory = prefs[Keys.SAVE_HISTORY] ?: true,
            autoCopy = prefs[Keys.AUTO_COPY] ?: false,
            clearAfterOperation = prefs[Keys.CLEAR_AFTER_OP] ?: false,
        )
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setSaveHistory(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SAVE_HISTORY] = enabled }
    }

    suspend fun setAutoCopy(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_COPY] = enabled }
    }

    suspend fun setClearAfterOperation(enabled: Boolean) {
        context.dataStore.edit { it[Keys.CLEAR_AFTER_OP] = enabled }
    }
}
