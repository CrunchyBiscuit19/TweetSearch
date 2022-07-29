package com.example.tweetsearch.screen

enum class Screen(
    val toolbarTitle: String,
    val accessedViaDrawer: Boolean = true
) {
    TweetPreview(
        toolbarTitle = "Select Screenshot"
    ),
    TweetInfo(
        toolbarTitle = "Screenshot Info",
        accessedViaDrawer = false
    ),
    Setting(
        toolbarTitle = "Settings",
        accessedViaDrawer = false
    );

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                TweetPreview.name -> TweetPreview
                TweetInfo.name -> TweetInfo
                Setting.name -> Setting
                null -> TweetPreview
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}