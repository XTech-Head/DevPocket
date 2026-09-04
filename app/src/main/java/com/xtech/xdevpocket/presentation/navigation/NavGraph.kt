package com.xtech.xdevpocket.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import com.xtech.xdevpocket.presentation.screens.base64.Base64Screen
import com.xtech.xdevpocket.presentation.screens.base64.Base64ViewModel
import com.xtech.xdevpocket.presentation.screens.caseconverter.CaseConverterScreen
import com.xtech.xdevpocket.presentation.screens.caseconverter.CaseConverterViewModel
import com.xtech.xdevpocket.presentation.screens.colorconverter.ColorConverterScreen
import com.xtech.xdevpocket.presentation.screens.colorconverter.ColorConverterViewModel
import com.xtech.xdevpocket.presentation.screens.commitmessage.CommitMessageScreen
import com.xtech.xdevpocket.presentation.screens.commitmessage.CommitMessageViewModel
import com.xtech.xdevpocket.presentation.screens.cron.CronScreen
import com.xtech.xdevpocket.presentation.screens.cron.CronViewModel
import com.xtech.xdevpocket.presentation.screens.gitignore.GitignoreScreen
import com.xtech.xdevpocket.presentation.screens.gitignore.GitignoreViewModel
import com.xtech.xdevpocket.presentation.screens.httpbuilder.HttpBuilderScreen
import com.xtech.xdevpocket.presentation.screens.httpbuilder.HttpBuilderViewModel
import com.xtech.xdevpocket.presentation.screens.randomstring.RandomStringScreen
import com.xtech.xdevpocket.presentation.screens.randomstring.RandomStringViewModel
import com.xtech.xdevpocket.presentation.screens.sql.SqlScreen
import com.xtech.xdevpocket.presentation.screens.sql.SqlViewModel
import com.xtech.xdevpocket.presentation.screens.xml.XmlScreen
import com.xtech.xdevpocket.presentation.screens.xml.XmlViewModel
import com.xtech.xdevpocket.presentation.screens.favorites.FavoritesScreen
import com.xtech.xdevpocket.presentation.screens.favorites.FavoritesViewModel
import com.xtech.xdevpocket.presentation.screens.hash.HashScreen
import com.xtech.xdevpocket.presentation.screens.hash.HashViewModel
import com.xtech.xdevpocket.presentation.screens.history.HistoryScreen
import com.xtech.xdevpocket.presentation.screens.history.HistoryViewModel
import com.xtech.xdevpocket.presentation.screens.home.HomeScreen
import com.xtech.xdevpocket.presentation.screens.home.HomeViewModel
import com.xtech.xdevpocket.presentation.screens.json.JsonScreen
import com.xtech.xdevpocket.presentation.screens.json.JsonViewModel
import com.xtech.xdevpocket.presentation.screens.jwt.JwtScreen
import com.xtech.xdevpocket.presentation.screens.jwt.JwtViewModel
import com.xtech.xdevpocket.presentation.screens.regex.RegexScreen
import com.xtech.xdevpocket.presentation.screens.regex.RegexViewModel
import com.xtech.xdevpocket.presentation.screens.settings.SettingsScreen
import com.xtech.xdevpocket.presentation.screens.settings.SettingsViewModel
import com.xtech.xdevpocket.presentation.screens.splash.SplashScreen
import com.xtech.xdevpocket.presentation.screens.timestamp.TimestampScreen
import com.xtech.xdevpocket.presentation.screens.timestamp.TimestampViewModel
import com.xtech.xdevpocket.presentation.screens.url.UrlScreen
import com.xtech.xdevpocket.presentation.screens.url.UrlViewModel
import com.xtech.xdevpocket.presentation.screens.uuid.UuidScreen
import com.xtech.xdevpocket.presentation.screens.uuid.UuidViewModel
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xtech.xdevpocket.presentation.theme.Aqua

private val topLevelRoutes = bottomNavItems.map { it.destination.route }

@Composable
fun XDevPocketNavGraph(factory: AppViewModelFactory) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

    val showBottomBar = currentRoute?.hierarchy?.any { dest ->
        topLevelRoutes.contains(dest.route)
    } == true

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute?.hierarchy?.any { it.route == item.destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (item.destination) {
                                        Destination.Home -> Icons.Filled.Home
                                        Destination.Favorites -> Icons.Filled.Star
                                        Destination.History -> Icons.Filled.History
                                        Destination.Settings -> Icons.Filled.Settings
                                        else -> Icons.Filled.Home
                                    },
                                    contentDescription = item.label,
                                )
                            },
                            label = { androidx.compose.material3.Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Aqua,
                                selectedTextColor = Aqua,
                                indicatorColor = Aqua.copy(alpha = 0.15f),
                            ),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Splash.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Destination.Splash.route) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(Destination.Splash.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(Destination.Home.route) {
                val vm: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = vm,
                    onToolClick = { tool -> navController.navigate(tool.route) },
                )
            }
            composable(Destination.Favorites.route) {
                val vm: FavoritesViewModel = viewModel(factory = factory)
                FavoritesScreen(
                    viewModel = vm,
                    onToolClick = { tool -> navController.navigate(tool.route) },
                )
            }
            composable(Destination.History.route) {
                val vm: HistoryViewModel = viewModel(factory = factory)
                HistoryScreen(
                    viewModel = vm,
                    onExploreTools = {
                        navController.navigate(Destination.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                        }
                    },
                )
            }
            composable(Destination.Settings.route) {
                val vm: SettingsViewModel = viewModel(factory = factory)
                SettingsScreen(viewModel = vm)
            }
            composable(Destination.JsonTool.route) {
                val vm: JsonViewModel = viewModel(factory = factory)
                JsonScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.Base64Tool.route) {
                val vm: Base64ViewModel = viewModel(factory = factory)
                Base64Screen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.UrlTool.route) {
                val vm: UrlViewModel = viewModel(factory = factory)
                UrlScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.HashTool.route) {
                val vm: HashViewModel = viewModel(factory = factory)
                HashScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.JwtTool.route) {
                val vm: JwtViewModel = viewModel(factory = factory)
                JwtScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.UuidTool.route) {
                val vm: UuidViewModel = viewModel(factory = factory)
                UuidScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.RegexTool.route) {
                val vm: RegexViewModel = viewModel(factory = factory)
                RegexScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.TimestampTool.route) {
                val vm: TimestampViewModel = viewModel(factory = factory)
                TimestampScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.CaseConverterTool.route) {
                val vm: CaseConverterViewModel = viewModel(factory = factory)
                CaseConverterScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.RandomStringTool.route) {
                val vm: RandomStringViewModel = viewModel(factory = factory)
                RandomStringScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.ColorConverterTool.route) {
                val vm: ColorConverterViewModel = viewModel(factory = factory)
                ColorConverterScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.CronHelperTool.route) {
                val vm: CronViewModel = viewModel(factory = factory)
                CronScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.XmlFormatterTool.route) {
                val vm: XmlViewModel = viewModel(factory = factory)
                XmlScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.SqlFormatterTool.route) {
                val vm: SqlViewModel = viewModel(factory = factory)
                SqlScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.HttpBuilderTool.route) {
                val vm: HttpBuilderViewModel = viewModel(factory = factory)
                HttpBuilderScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.GitignoreGeneratorTool.route) {
                val vm: GitignoreViewModel = viewModel(factory = factory)
                GitignoreScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
            composable(Destination.CommitMessageBuilderTool.route) {
                val vm: CommitMessageViewModel = viewModel(factory = factory)
                CommitMessageScreen(viewModel = vm, onBack = { navController.popBackStack() }, snackbarHostState = snackbarHostState)
            }
        }
    }
}
