package com.example.tweetsearch.data.search

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

const val TWITTER_MAX_QUERY_LENGTH = 512

data class SearchData(
    val handle: String?,
    val content: String,
) {
    fun generateQuery(): String {
        val handlePart = "from:${handle.toString().replace("@", "")}"
        val contentPart = content
            .trim()
            .split(" ")
            .filter {
                it.length > 3
            }
            .joinToString(" OR ", prefix = "(")
        return "${("$handlePart $contentPart").take(TWITTER_MAX_QUERY_LENGTH - 1)})"
    }
}

class SearchDataHelper(unparsedTweet: String) {
    private val unparsedContent = unparsedTweet

    fun createSearchData(): SearchData {
        return SearchData(
            handle = if (processHandles().isEmpty()) null else processHandles()[0],
            content = processContent()
        )
    }

    fun processHandles(): List<String> {
        val handleRegex = Regex("@[A-Za-z0-9_]{4,15}")
        return handleRegex.findAll(unparsedContent).toList().map { result ->
            result.value
        }.distinct()
    }

    fun processDates(): List<LocalDate?> {
        val dates = mutableListOf<LocalDate?>()
        val dateRegexes = mapOf(
            "d/M/yy" to Regex("\\d/\\d/\\d{2}"),                                // 2/2/18
            "dd/MM/yy" to Regex("\\d{2}/\\d{2}/\\d{2}"),                        // 02/02/18
            "d/M/yyyy" to Regex("\\d/\\d/\\d{4}"),                              // 2/2/2018
            "dd/MM/yyyy" to Regex("\\d{1,2}/\\d{1,2}/\\d{4}"),                  // 02/02/2018,
            "d MMM yy" to Regex("\\d\\s?[A-Za-z]{3}\\s?\\d{2}"),                // 2 Feb 18
            "dd MMM yy" to Regex("\\d{1,2}\\s?[A-Za-z]{3}\\s?\\d{2}"),          // 02 Feb 18
            "d MMM yyyy" to Regex("\\d\\s?[A-Za-z]{3}\\s?\\d{4}"),              // 2 Feb 2018
            "dd MMM yyyy" to Regex("\\d{1,2}\\s?[A-Za-z]{3}\\s?\\d{4}"),        // 02 Feb 2018
            "MMM d, yyyy" to Regex("[A-Za-z]{3}\\s?\\d{1,2},\\s?\\d{4}"),       // Feb 2, 2018
            "MMM dd, yyyy" to Regex("[A-Za-z]{3}\\s?\\d{1,2},\\s?\\d{4}"),      // Feb 02, 2018
        )
        dateRegexes.forEach { (dateFormat, dateRegex) ->
            dates += dateRegex.findAll(unparsedContent).toList().map { result ->
                try {
                    LocalDate.parse(
                        result.value,
                        DateTimeFormatter.ofPattern(dateFormat, Locale.US)
                    )
                } catch (e: DateTimeParseException) {
                    null
                }
            }
        }
        return dates.distinct().filterIsInstance<LocalDate>()
    }

    fun processContent(): String {
        val noSpecialCharsRegex = Regex("[^A-Za-z \\n]+")
        val newlinesRegex = Regex("\n+")
        val multipleSpacesRegex = Regex(" +")

        return unparsedContent
            .replace(noSpecialCharsRegex, " ")
            .replace(newlinesRegex, " ")
            .replace(multipleSpacesRegex, " ")
    }
}

