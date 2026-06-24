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


// Theme.kt

import androidx.compose.ui.graphics.Color

// --- Твои цвета ---
private val MyBlue = Color(0xFF1D4ED8)
private val MyBlueDark = Color(0xFF93C5FD)
private val MySurface = Color(0xFFF8FAFC)
private val MyBackground = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary = MyBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF0EA5E9),
    background = MyBackground,
    surface = MySurface,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF64748B),
)

private val DarkColors = darkColorScheme(
    primary = MyBlueDark,
    onPrimary = Color(0xFF1E293B),
    primaryContainer = Color(0xFF1D4ED8),
    secondary = Color(0xFF38BDF8),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
)

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
        colorScheme = if (isDark) DarkColors else LightColors,
        content = content
    )
}
