package com.xtech.xdevpocket.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xtech.xdevpocket.R
import com.xtech.xdevpocket.data.preferences.AppTheme
import com.xtech.xdevpocket.presentation.components.SectionHeader
import com.xtech.xdevpocket.presentation.components.SupportLinkHelper
import com.xtech.xdevpocket.presentation.theme.Aqua
import com.xtech.xdevpocket.presentation.theme.Danger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val prefs by viewModel.preferences.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(20.dp))

        SectionHeader("Appearance")
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            AppTheme.entries.forEachIndexed { index, theme ->
                SegmentedButton(
                    selected = prefs.theme == theme,
                    onClick = { viewModel.setTheme(theme) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = AppTheme.entries.size),
                    colors = SegmentedButtonDefaults.colors(activeContainerColor = Aqua.copy(alpha = 0.18f)),
                ) {
                    Text(theme.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionHeader("Behavior")
        SettingsSwitchRow(
            title = "Save history",
            checked = prefs.saveHistory,
            onCheckedChange = viewModel::setSaveHistory,
        )
        SettingsSwitchRow(
            title = "Auto-copy results",
            checked = prefs.autoCopy,
            onCheckedChange = viewModel::setAutoCopy,
        )
        SettingsSwitchRow(
            title = "Clear input after operation",
            checked = prefs.clearAfterOperation,
            onCheckedChange = viewModel::setClearAfterOperation,
        )

        Spacer(Modifier.height(24.dp))
        SectionHeader("Data")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(onClick = viewModel::clearHistory) {
                Text("Clear history", color = Danger)
            }
            TextButton(onClick = viewModel::clearFavorites) {
                Text("Clear favorites", color = Danger)
            }
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        SectionHeader("Support")
        Text(
            "x-DevPocket is built and maintained independently by Sammy. If it saves you " +
                "time, buying a coffee helps keep it free, ad-free, and offline-first.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { SupportLinkHelper.openBuyMeACoffee(context) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Aqua, contentColor = MaterialTheme.colorScheme.background),
        ) {
            Icon(Icons.Filled.LocalCafe, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text("Buy Sammy a Coffee")
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        SectionHeader("About")
        Image(
            painter = painterResource(id = R.drawable.logo_xdevpocket),
            contentDescription = "x-DevPocket",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(64.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Developer toolkit for Android.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Version 1.0.0\nBuilt with Kotlin + Jetpack Compose\nOffline-first",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "A product of Xtech Devs, led by Samuel Njoroge.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = Aqua, checkedThumbColor = MaterialTheme.colorScheme.background),
        )
    }
}
