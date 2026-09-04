package com.xtech.xdevpocket

import android.app.Application
import com.xtech.xdevpocket.data.local.AppDatabase
import com.xtech.xdevpocket.data.preferences.PreferencesManager
import com.xtech.xdevpocket.data.repository.DeveloperRepository

/**
 * Composition root. x-DevPocket is intentionally dependency-injection-framework-free —
 * the app is small enough that a hand-rolled container keeps things simple and offline.
 */
class XDevPocketApp : Application() {

    lateinit var repository: DeveloperRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = AppDatabase.getInstance(this)
        val preferencesManager = PreferencesManager(this)
        repository = DeveloperRepository(
            historyDao = database.historyDao(),
            favoriteDao = database.favoriteDao(),
            preferencesManager = preferencesManager,
        )
    }
}
