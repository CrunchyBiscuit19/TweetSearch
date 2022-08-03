package com.example.tweetsearch.data.api

@Suppress("unused")
enum class ApiKeys {
    TWITTER_API_KEY,
    TWITTER_API_KEY_SECRET,
    TWITTER_BEARER_TOKEN,
    TWITTER_ACCESS_TOKEN,
    TWITTER_ACCESS_TOKEN_SECRET;

    companion object {
        fun getEnvWithFallback(envVariable: String, fallback: String = envVariable): String {
            return System.getenv(envVariable).takeUnless { it.isNullOrEmpty() } ?: fallback
        }
    }
}