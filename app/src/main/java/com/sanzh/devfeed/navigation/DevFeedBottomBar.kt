package com.sanzh.devfeed.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun DevFeedBottomBar(navController: NavController) {
    val tabs = listOf(
        Triple(Screen.Feed, Icons.Default.Home, "Feed"),
        Triple(Screen.Bookmarks, Icons.Default.Bookmark, "Bookmarks"),
        Triple(Screen.Settings, Icons.Default.Settings, "Settings")
    )
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route
    NavigationBar {
        tabs.forEach { (screen, icon, label) ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}