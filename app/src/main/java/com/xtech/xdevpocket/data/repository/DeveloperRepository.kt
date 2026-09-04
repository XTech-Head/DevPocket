package com.xtech.xdevpocket.data.repository

import com.xtech.xdevpocket.data.local.FavoriteDao
import com.xtech.xdevpocket.data.local.FavoriteEntity
import com.xtech.xdevpocket.data.local.HistoryDao
import com.xtech.xdevpocket.data.local.HistoryEntity
import com.xtech.xdevpocket.data.preferences.PreferencesManager
import com.xtech.xdevpocket.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Single access point for local persistence. UI and ViewModels never talk to
 * Room or DataStore directly — everything goes through here.
 */
class DeveloperRepository(
    private val historyDao: HistoryDao,
    private val favoriteDao: FavoriteDao,
    private val preferencesManager: PreferencesManager,
) {

    // History
    fun observeHistory(): Flow<List<HistoryEntity>> = historyDao.observeAll()

    suspend fun addHistory(tool: String, input: String, output: String) {
        historyDao.insert(
            HistoryEntity(
                tool = tool,
                input = input,
                output = output,
                timestamp = System.currentTimeMillis(),
            )
        )
    }

    suspend fun deleteHistory(entity: HistoryEntity) = historyDao.delete(entity)

    suspend fun clearHistory() = historyDao.deleteAll()

    // Favorites
    fun observeFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.observeAll()

    suspend fun toggleFavorite(toolId: String, currentlyFavorite: Boolean) {
        if (currentlyFavorite) {
            favoriteDao.deleteById(toolId)
        } else {
            favoriteDao.insert(FavoriteEntity(toolId = toolId, timestamp = System.currentTimeMillis()))
        }
    }

    suspend fun clearFavorites() = favoriteDao.deleteAll()

    // Preferences
    val preferences: Flow<UserPreferences> = preferencesManager.preferencesFlow

    suspend fun setTheme(theme: com.xtech.xdevpocket.data.preferences.AppTheme) =
        preferencesManager.setTheme(theme)

    suspend fun setSaveHistory(enabled: Boolean) = preferencesManager.setSaveHistory(enabled)

    suspend fun setAutoCopy(enabled: Boolean) = preferencesManager.setAutoCopy(enabled)

    suspend fun setClearAfterOperation(enabled: Boolean) =
        preferencesManager.setClearAfterOperation(enabled)
}
