package com.sanzh.devfeed.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sanzh.devfeed.DevFeedApp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = remember { (context.applicationContext as
            DevFeedApp).preferencesManager }
    val scope = rememberCoroutineScope()
    val isDark by prefs.isDarkMode.collectAsState(initial = false)
    val language by prefs.languageFilter.collectAsState(initial = "kotlin")
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))
        // Dark mode toggle — writes to DataStore
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDark,
                onCheckedChange = { scope.launch { prefs.setDarkMode(it) } }
            )
        }
        Spacer(Modifier.height(16.dp))
        Text("Language Filter", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        // Language chips — writes to DataStore
        val langs = listOf("kotlin", "swift", "python", "typescript", "rust")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(langs) { lang ->
                FilterChip(
                    selected = language == lang,
                    onClick = { scope.launch { prefs.setLanguage(lang) } },
                    label = { Text(lang) }
                )
            }
        }
    }
}
