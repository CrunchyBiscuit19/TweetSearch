package com.example.tweetsearch.data.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tweetsearch.data.api.ApiKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder
import java.util.concurrent.ConcurrentHashMap

class TwitterApiViewModel(unparsedTweet: String) : ViewModel() {
    private val twitter: Twitter
    private val searchDataHelper = SearchDataHelper(unparsedTweet)

    init {
        val twitterConfiguration = ConfigurationBuilder()
            .setJSONStoreEnabled(true)
            .setOAuthConsumerKey(ApiKeys.TWITTER_API_KEY.key)
            .setOAuthConsumerSecret(ApiKeys.TWITTER_API_KEY_SECRET.key)
            .setOAuthAccessToken(ApiKeys.TWITTER_ACCESS_TOKEN.key)
            .setOAuthAccessTokenSecret(ApiKeys.TWITTER_ACCESS_TOKEN_SECRET.key)
            .build()
        twitter = TwitterFactory(twitterConfiguration).instance
    }

    var processedContent = MutableStateFlow(searchDataHelper.processContent())

    var tweetMatches: MutableStateFlow<ConcurrentHashMap<Tweet, User2>?> = MutableStateFlow(null)
    val tweetApiError = MutableStateFlow(false)
    fun searchTweet() {
        viewModelScope.launch(Dispatchers.IO) {
            val searchData = searchDataHelper.createSearchData()
            try {
                twitter.searchRecent(
                    searchData.generateQuery(),
                    tweetFields = "created_at,public_metrics,author_id",
                    userFields = "profile_image_url,username",
                )
                    .let { result ->
                        // Add the author handle and screenname to each tweet
                        val tweetMatchesBuffer = ConcurrentHashMap<Tweet, User2>()
                        result.tweets.forEach { tweet ->
                            twitter.getUsers(tweet.authorId!!.toLong()).let {
                                val author = it.users[0]
                                tweetMatchesBuffer[tweet] = author
                            }
                        }
                        tweetMatches.value = tweetMatchesBuffer
                    }
            } catch (e:TwitterException) {
                tweetApiError.value = true
            }
        }
    }
}

class TwitterApiViewModelFactory(private val unparsedTweet: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TwitterApiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TwitterApiViewModel(unparsedTweet) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}