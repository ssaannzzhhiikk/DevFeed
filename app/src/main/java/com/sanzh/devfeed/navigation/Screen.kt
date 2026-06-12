package com.sanzh.devfeed.navigation

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Bookmarks : Screen("bookmarks")
    object Settings : Screen("settings")
    object Detail : Screen("detail/{itemId}/{itemType}") {
        const val ROUTE = "detail/{itemId}/{itemType}"
        fun createRoute(id: Long, type: String) = "detail/$id/$type"
    }
}
