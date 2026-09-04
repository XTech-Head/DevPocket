package com.xtech.xdevpocket.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.xtech.xdevpocket.data.repository.DeveloperRepository
import com.xtech.xdevpocket.presentation.screens.base64.Base64ViewModel
import com.xtech.xdevpocket.presentation.screens.caseconverter.CaseConverterViewModel
import com.xtech.xdevpocket.presentation.screens.colorconverter.ColorConverterViewModel
import com.xtech.xdevpocket.presentation.screens.commitmessage.CommitMessageViewModel
import com.xtech.xdevpocket.presentation.screens.cron.CronViewModel
import com.xtech.xdevpocket.presentation.screens.gitignore.GitignoreViewModel
import com.xtech.xdevpocket.presentation.screens.httpbuilder.HttpBuilderViewModel
import com.xtech.xdevpocket.presentation.screens.randomstring.RandomStringViewModel
import com.xtech.xdevpocket.presentation.screens.sql.SqlViewModel
import com.xtech.xdevpocket.presentation.screens.xml.XmlViewModel
import com.xtech.xdevpocket.presentation.screens.favorites.FavoritesViewModel
import com.xtech.xdevpocket.presentation.screens.hash.HashViewModel
import com.xtech.xdevpocket.presentation.screens.history.HistoryViewModel
import com.xtech.xdevpocket.presentation.screens.home.HomeViewModel
import com.xtech.xdevpocket.presentation.screens.json.JsonViewModel
import com.xtech.xdevpocket.presentation.screens.jwt.JwtViewModel
import com.xtech.xdevpocket.presentation.screens.regex.RegexViewModel
import com.xtech.xdevpocket.presentation.screens.settings.SettingsViewModel
import com.xtech.xdevpocket.presentation.screens.timestamp.TimestampViewModel
import com.xtech.xdevpocket.presentation.screens.url.UrlViewModel
import com.xtech.xdevpocket.presentation.screens.uuid.UuidViewModel

/**
 * A single hand-rolled ViewModelProvider.Factory. Keeps the app free of a DI
 * framework dependency while still giving every screen a properly scoped ViewModel
 * backed by the shared repository.
 */
class AppViewModelFactory(private val repository: DeveloperRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(repository) as T
            JsonViewModel::class.java -> JsonViewModel(repository) as T
            Base64ViewModel::class.java -> Base64ViewModel(repository) as T
            UrlViewModel::class.java -> UrlViewModel(repository) as T
            HashViewModel::class.java -> HashViewModel(repository) as T
            JwtViewModel::class.java -> JwtViewModel(repository) as T
            UuidViewModel::class.java -> UuidViewModel(repository) as T
            RegexViewModel::class.java -> RegexViewModel(repository) as T
            TimestampViewModel::class.java -> TimestampViewModel(repository) as T
            HistoryViewModel::class.java -> HistoryViewModel(repository) as T
            FavoritesViewModel::class.java -> FavoritesViewModel(repository) as T
            SettingsViewModel::class.java -> SettingsViewModel(repository) as T
            CaseConverterViewModel::class.java -> CaseConverterViewModel(repository) as T
            RandomStringViewModel::class.java -> RandomStringViewModel(repository) as T
            ColorConverterViewModel::class.java -> ColorConverterViewModel(repository) as T
            CronViewModel::class.java -> CronViewModel(repository) as T
            XmlViewModel::class.java -> XmlViewModel(repository) as T
            SqlViewModel::class.java -> SqlViewModel(repository) as T
            HttpBuilderViewModel::class.java -> HttpBuilderViewModel(repository) as T
            GitignoreViewModel::class.java -> GitignoreViewModel(repository) as T
            CommitMessageViewModel::class.java -> CommitMessageViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
