package com.example.tweetsearch

enum class Screen(
    val toolbarTitle: String
) {
    TweetPreview(
        toolbarTitle = "Select Screenshot"
    ),
    TweetInfo(
        toolbarTitle = "Screenshot Info"
    );

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                TweetPreview.name -> TweetPreview
                TweetInfo.name -> TweetInfo
                null -> TweetPreview
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}