package com.sanzh.devfeed.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.sanzh.devfeed.DevFeedApp

@Composable
fun DevFeedTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val systemInDark = isSystemInDarkTheme()
    
    // Observe DataStore dark mode preference with safety for Previews
    val isDark = if (LocalInspectionMode.current) {
        systemInDark
    } else {
        val prefs = remember { (context.applicationContext as DevFeedApp).preferencesManager }
        prefs.isDarkMode.collectAsState(initial = systemInDark).value
    }

    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme(),
        content = content
    )
}
