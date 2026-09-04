package com.xtech.xdevpocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.xtech.xdevpocket.data.preferences.AppTheme
import com.xtech.xdevpocket.presentation.navigation.AppViewModelFactory
import com.xtech.xdevpocket.presentation.navigation.XDevPocketNavGraph
import com.xtech.xdevpocket.presentation.theme.ThemeMode
import com.xtech.xdevpocket.presentation.theme.XDevPocketTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be called before super.onCreate() — this is what shows the brief
        // system splash (brand mark on the app background) during cold start,
        // before our own Compose splash screen (with the full logo) takes over.
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as XDevPocketApp
        val factory = AppViewModelFactory(app.repository)

        setContent {
            val prefs by app.repository.preferences.collectAsState(
                initial = com.xtech.xdevpocket.data.preferences.UserPreferences()
            )

            val themeMode = when (prefs.theme) {
                AppTheme.DARK -> ThemeMode.DARK
                AppTheme.LIGHT -> ThemeMode.LIGHT
                AppTheme.SYSTEM -> ThemeMode.SYSTEM
            }

            XDevPocketTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background,
                ) {
                    XDevPocketNavGraph(factory = factory)
                }
            }
        }
    }
}
