package com.example.tweetsearch.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(
    val toolbarTitle: String,
    val drawerTitle: String = toolbarTitle,
    val icon: ImageVector = Icons.Filled.Close,
    val accessedViaDrawer: Boolean = true
) {
    TweetPreview(
        toolbarTitle = "Select Screenshot",
        drawerTitle = "Search",
        icon = Icons.Filled.Search
    ),
    TweetInfo(
        toolbarTitle = "Screenshot Info",
        accessedViaDrawer = false,
    ),
    SearchHistory(
        toolbarTitle = "Search History",
        drawerTitle = "History",
        icon = Icons.Filled.History,
    ),
    Setting(
        toolbarTitle = "Settings",
        icon = Icons.Filled.Settings
    ),
    Instruction(
        toolbarTitle = "Instructions",
        icon = Icons.Filled.Description
    );

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                TweetPreview.name -> TweetPreview
                TweetInfo.name -> TweetInfo
                SearchHistory.name -> SearchHistory
                Setting.name -> Setting
                Instruction.name -> Instruction
                null -> TweetPreview
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}