package com.example.tweetsearch.screen

enum class Screen(
    val toolbarTitle: String
) {
    TweetPreview(
        toolbarTitle = "Select Screenshot"
    ),
    TweetInfo(
        toolbarTitle = "Screenshot Info"
    ),
    Setting(
        toolbarTitle = "Settings"
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