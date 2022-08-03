package com.example.tweetsearch.data.search

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

data class SearchCriteria(
    val handle: String,
    val date: LocalDate,
    val content: String
)

class SearchCriteriaBuilder(private val unparsedTweet: String) {

    fun findHandles(unparsedTweet: String): List<String> {
        val handleRegex = Regex("@[A-Za-z0-9_]{4,15}")
        return handleRegex.findAll(unparsedTweet).toList().map { result ->
            result.value
        }.distinct()
    }

    fun findDates(unparsedTweet: String): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
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
        val placeholderDate = LocalDate.of(1970, 1, 1)
        dateRegexes.forEach { (dateFormat, dateRegex) ->
            dates += dateRegex.findAll(unparsedTweet).toList().map { result ->
                try {
                    LocalDate.parse(
                        result.value,
                        DateTimeFormatter.ofPattern(dateFormat, Locale.US)
                    )
                } catch (e: DateTimeParseException) {
                    placeholderDate
                }
            }
        }
        return dates.distinct().sorted()
            .filterNot { it.year == 1970 && it.monthValue == 1 && it.dayOfMonth == 1 }
    }

    fun processContent(unparsedTweet: String): String {
        val noSpecialCharsRegex = Regex("[^A-Za-z \\n]+")
        val newlinesRegex = Regex("\n+")
        val multipleSpacesRegex = Regex(" +")

        return unparsedTweet
            .replace(noSpecialCharsRegex, " ")
            .replace(newlinesRegex, " ")
            .replace(multipleSpacesRegex, " ")
    }
}

