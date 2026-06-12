package com.sanzh.devfeed.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sanzh.devfeed.ui.bookmarks.BookmarksScreen
import com.sanzh.devfeed.ui.detail.DetailScreen
import com.sanzh.devfeed.ui.feed.FeedScreen
import com.sanzh.devfeed.ui.settings.SettingsScreen

@Composable
fun DevFeedApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { DevFeedBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Feed.route) {
                FeedScreen(onItemClick = { id, type ->
                    // Navigate to Detail — this is the master-detail pattern
                    navController.navigate(Screen.Detail.createRoute(id, type))
                })
            }
            composable(Screen.Bookmarks.route) {
                BookmarksScreen(onItemClick = { id, type ->
                    navController.navigate(Screen.Detail.createRoute(id, type))
                })
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.Detail.ROUTE,
                arguments = listOf(
                    navArgument("itemId") { type = NavType.LongType },
                    navArgument("itemType") { type = NavType.StringType }
                )
            ) { entry ->
                val id = entry.arguments!!.getLong("itemId")
                val type = entry.arguments!!.getString("itemType")!!
                // Arguments passed FROM master (Feed) TO detail — communication
                DetailScreen(
                    itemId = id, itemType = type,
                    onBack = { navController.popBackStack() })
            }
        }
    }
}
